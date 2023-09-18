package com.nttdata.bootcamp.s01accountservice.serviceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.nttdata.bootcamp.s01accountservice.feingClient.CreditFeignClient;
import com.nttdata.bootcamp.s01accountservice.feingClient.TransactionFeignClient;
import com.nttdata.bootcamp.s01accountservice.model.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nttdata.bootcamp.s01accountservice.document.AccountDocument;
import com.nttdata.bootcamp.s01accountservice.exception.AccountCreationException;
import com.nttdata.bootcamp.s01accountservice.feingClient.ClienteFeignClient;
import com.nttdata.bootcamp.s01accountservice.mapper.AccountMapper;
import com.nttdata.bootcamp.s01accountservice.repository.AccountMongoRepository;
import com.nttdata.bootcamp.s01accountservice.service.AccountService;
@Service
public class AccountServiceImpl implements AccountService {
/**
 * injeccion clienteFeignClient.
 */
	@Autowired
	private ClienteFeignClient clienteFeignClient;

	/**
	 * injeccion CreditFeignClient.
	 */
	@Autowired
	private CreditFeignClient creditFeignClient;

	/**
	 * injeccion TransactionFeignClient.
	 */
	@Autowired
	private TransactionFeignClient transactionFeignClient;

	/**
	 * injeccion AccountMongoRepository.
	 */
	@Autowired
	private AccountMongoRepository accountMongoRepository;

	/**
	 * AccountService.
	 * @param accountId busqueda por id.
	 * @return un AccountDetails.
	 */
	@Override
	public AccountDetails accountsAccountIdGet(final String accountId) {
		return AccountMapper.mapDocumentToDto(accountMongoRepository.findById(new ObjectId(accountId)));
	}

	/**
	 * AccountService.
	 * @param requestBody el cuerpo.
	 * @return una lista AccountDetails.
	 */
	@Override
	public List<AccountDetails> accountsByListPost(final List<String> requestBody) {
		return accountMongoRepository
				.findByOwnerClientsContains(requestBody.stream().map(ObjectId::new).collect(Collectors.toList()));
	}

	/**
	 * AccountService.
	 * @param accountCreateInput type.
	 * @return un AccountDetails.
	 */
	@Override
	public AccountDetails accountsPost(final AccountCreateInput accountCreateInput) {
		AccountDocument savedAccount;
		List<ClientDTO> dtoList = clienteFeignClient.bulkRetrieveClients(accountCreateInput.getOwnerClients());

		List<CreditCardDetails> allCreditCardDetails = dtoList.stream()
				.flatMap(clientDTO -> creditFeignClient.getCreditCardsByClientId(clientDTO.getId()).stream())
				.collect(Collectors.toList());

		Predicate<AccountCreateInput> isAhorroAccount = input -> input.getType().equals(AccountCreateInput.TypeEnum.AHORRO);
		Predicate<ClientDTO> isVipClient = client -> client.getTipoCliente() != null && "personal".equalsIgnoreCase(client.getTipoCliente().getNombre()) && "VIP".equalsIgnoreCase(client.getTipoCliente().getPerfil().getNombre());
		Predicate<ClientDTO> isPersonalClient = client -> client.getTipoCliente() != null && "personal".equalsIgnoreCase(client.getTipoCliente().getNombre());
		Predicate<ClientDTO> isEmpresarialClient = client -> client.getTipoCliente() != null && "empresarial".equalsIgnoreCase(client.getTipoCliente().getNombre());

		List<ClientDTO> personalClients = dtoList.stream().filter(isPersonalClient).collect(Collectors.toList());
		List<ClientDTO> empresarialClients = dtoList.stream().filter(isEmpresarialClient).collect(Collectors.toList());

		savedAccount = AccountMapper.mapCreateInputToDocument(accountCreateInput);

		// Validación y creación para cliente personal
		if (!personalClients.isEmpty()) {
			boolean accountExists = personalClients.stream().anyMatch(personalClient -> {
				List<AccountDetails> accountsOfType = accountMongoRepository
						.findByOwnerClientsContainsAndType(new ObjectId(personalClient.getId()), accountCreateInput.getType())
						.stream().map(AccountMapper::mapDocumentToDto).collect(Collectors.toList());
				return !accountsOfType.isEmpty();
			});

			if (accountExists) {
				throw new AccountCreationException("El cliente personal ya tiene una cuenta de este tipo.");
			}

			if (isAhorroAccount.test(accountCreateInput)) {
				if (personalClients.stream().allMatch(isVipClient)) {
					if (allCreditCardDetails.isEmpty()) {
						throw new AccountCreationException("No tienes tarjetas de crédito asociadas a este cliente.");
					}
					savedAccount.setBalance(new BigDecimal(500));
				}
			}

			savedAccount = accountMongoRepository.save(savedAccount);
			return AccountMapper.mapDocumentToDto(savedAccount);
		}

		if (!empresarialClients.isEmpty()) {
			if (allCreditCardDetails.isEmpty()) {
				throw new AccountCreationException("No hay tarjetas de crédito asociadas al cliente empresarial.");
			}

			if (isAhorroAccount.test(accountCreateInput) || accountCreateInput.getType().equals(AccountCreateInput.TypeEnum.PLAZOFIJO)) {
				throw new AccountCreationException("Un cliente empresarial no puede tener una cuenta de ahorro o de plazo fijo.");
			}

			if (accountCreateInput.getSignClients().size() >= 4) {
				throw new AccountCreationException("Una cuenta empresarial no puede tener más de 4 firmantes autorizados.");
			}

			if (accountCreateInput.getType().equals(AccountCreateInput.TypeEnum.CORRIENTE)) {
				List<AccountDetails> existingCorrienteAccounts = accountMongoRepository
						.findByOwnerClientsContainsAndType(new ObjectId(empresarialClients.get(0).getId()), AccountCreateInput.TypeEnum.CORRIENTE)
						.stream().map(AccountMapper::mapDocumentToDto).collect(Collectors.toList());
				if (!existingCorrienteAccounts.isEmpty()) {
					throw new AccountCreationException("Un cliente empresarial ya tiene una cuenta corriente.");
				}
			}

			savedAccount = accountMongoRepository.save(savedAccount);
			return AccountMapper.mapDocumentToDto(savedAccount);
		}
		throw new AccountCreationException("No se pudo crear la cuenta.");
	}

	/**
	 * AccountService.
	 * @param requestBody lista del cuerpo.
	 * @param accountId variable.
	 * @return un AccountDetails.
	 */
	@Override
	public AccountDetails accountsAccountIdAddSignersPost(final String accountId, final List<String> requestBody) {
		AccountDocument savedAccount = null;
		Predicate<Set<String>> moreThan4Elements = (set) -> set.size() >= 4;
		AccountDocument accountDocument = accountMongoRepository.findById(new ObjectId(accountId));

		if (accountDocument == null) {
			throw new AccountCreationException("La cuenta solicitada no existe");
		}

		List<String> signClientIds = accountDocument.getSignClients();
		Set<String> uniqueIds = Stream.concat(signClientIds.stream(), requestBody.stream()).collect(Collectors.toSet());

		if (moreThan4Elements.test(uniqueIds)) {
			throw new AccountCreationException("Una cuenta empresarial no puede tener más de 4 firmantes autorizados.");
		} else {
			List<String> signersToAdd = uniqueIds.stream().collect(Collectors.toList());
			accountDocument.setSignClients(signersToAdd);
			savedAccount = accountMongoRepository.save(accountDocument);
		}
		return AccountMapper.mapDocumentToDto(savedAccount);
	}

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	@Override
	public AccountDetails accountsDepositPost(final TransactionInput transactionInput) {
		return transaction(transactionInput, true);
	}

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	@Override
	public AccountDetails accountsWithdrawPost(final TransactionInput transactionInput) {
		return transaction(transactionInput, false);
	}

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @param isDeposit variable.
	 * @return AccountMapper.mapDocumentToDto(account).
	 */
	private AccountDetails transaction(final TransactionInput transactionInput, final boolean isDeposit) {
		Optional<AccountDocument> optAccount = accountMongoRepository.findById(transactionInput.getAccountId());

		if (!optAccount.isPresent()) {
			throw new IllegalArgumentException("Account not found.");
		}

		AccountDocument account = optAccount.get();

		if (account.getLastTransactionDate() != null
				&& !account.getLastTransactionDate().getMonth().equals(LocalDate.now().getMonth())) {
			account.setTransactionCount(0);
		}

		BigDecimal amount = transactionInput.getAmount();

		Predicate<AccountDocument> isBeyondFreeTransactionLimit = acc -> acc.getTransactionCount() > 20;

		BigDecimal commissionAmount = BigDecimal.ZERO;

		if (isDeposit) {
			if (isBeyondFreeTransactionLimit.test(account)) {
				commissionAmount = amount.multiply(new BigDecimal("0.02"));
				amount = amount.subtract(commissionAmount);
			}
			account.setBalance(account.getBalance().add(amount));
		} else {
			if (isBeyondFreeTransactionLimit.test(account)) {
				commissionAmount = amount.multiply(new BigDecimal("0.02"));
				amount = amount.add(commissionAmount);
			}
			if (account.getBalance().compareTo(amount) < 0) {
				throw new IllegalArgumentException("Insufficient funds.");
			}
			account.setBalance(account.getBalance().subtract(amount));
		}

		if (commissionAmount.compareTo(BigDecimal.ZERO) > 0) {
			CommissionDTO commissionDTO = new CommissionDTO();
			commissionDTO.setAccountId(account.getId());
			commissionDTO.setTransactionType(isDeposit ? CommissionDTO.TransactionTypeEnum.DEPOSIT : CommissionDTO.TransactionTypeEnum.WITHDRAW);
			commissionDTO.setTransactionDate(OffsetDateTime.now());
			commissionDTO.setComision(commissionAmount);
			try {
				transactionFeignClient.registerCommission(commissionDTO);
			} catch (Exception e) {
				System.err.println("Error al registrar la comisión con el microservicio: " + e.getMessage());
			}
		}
		account.setTransactionCount(account.getTransactionCount() + 1);
		accountMongoRepository.save(account);
		return AccountMapper.mapDocumentToDto(account);
	}
}
