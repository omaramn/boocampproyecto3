package com.nttdata.bootcamp.s01accountservice.apiDelegateimpl;
import java.util.List;
import com.nttdata.bootcamp.s01accountservice.model.TransactionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.nttdata.bootcamp.s01accountservice.api.AccountsApiDelegate;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
import com.nttdata.bootcamp.s01accountservice.service.AccountService;
@Component
public class AccoutApiDelegateImpl implements AccountsApiDelegate {
	/**
	 * Para acceder a accountService.
	 */
	@Autowired
	private AccountService accountService;

	/**
	 * Método para guardar una transacción.
	 * @param accountId parametro de AccountDetails.
	 * @return accountsAccountIdGet del accountId.
	 */
	@Override
	public ResponseEntity<AccountDetails> accountsAccountIdGet(final String accountId) {
		return ResponseEntity.ok(accountService.accountsAccountIdGet(accountId));
	}

	/**
	 * Método para guardar una transacción.
	 * @param requestBody parametro de AccountDetails.
	 * @return accountsByListPost del requestBody.
	 */
	@Override
	public ResponseEntity<List<AccountDetails>> accountsByListPost(final List<String> requestBody) {
		return ResponseEntity.ok(accountService.accountsByListPost(requestBody));
	}

	/**
	 * Método para guardar una transacción.
	 * @param clientId parametro de AccountDetails.
	 * @return accountsClientIdGet del clientId.
	 */
	@Override
	public ResponseEntity<AccountDetails> accountsClientIdGet(final String clientId) {
		return AccountsApiDelegate.super.accountsClientIdGet(clientId);
	}

	/**
	 * Método para guardar una transacción.
	 * @param accountCreateInput parametro de AccountDetails.
	 * @return accountsPost del accountCreateInput.
	 */
	@Override
	public ResponseEntity<AccountDetails> accountsPost(final AccountCreateInput accountCreateInput) {
		return ResponseEntity.ok(accountService.accountsPost(accountCreateInput));
	}

	/**
	 * Método para guardar una transacción.
	 * @param accountId parametro para buscar.
	 * @param requestBody lista del body.
	 * @return accountsAccountIdAddSignersPost de los accountId, requestBody
	 */
	@Override
	public ResponseEntity<AccountDetails> accountsAccountIdAddSignersPost(final String accountId, final List<String> requestBody) {
		return ResponseEntity.ok(accountService.accountsAccountIdAddSignersPost(accountId, requestBody));
	}

	/**
	 * Método para guardar una transacción.
	 * @param transactionInput parametro de AccountDetails.
	 * @return accountsDepositPost del transactionInput.
	 */
	@Override
	public ResponseEntity<AccountDetails> accountsDepositPost(final TransactionInput transactionInput) {
		return AccountsApiDelegate.super.accountsDepositPost(transactionInput);
	}

	/**
	 * Método para guardar una transacción.
	 * @param transactionInput parametro de AccountDetails.
	 * @return accountsWithdrawPost del transactionInput.
	 */
	@Override
	public ResponseEntity<AccountDetails> accountsWithdrawPost(final TransactionInput transactionInput) {
		return AccountsApiDelegate.super.accountsWithdrawPost(transactionInput);
	}
}


