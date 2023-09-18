package com.nttdata.bootcamp.s01accountservice.service;
import java.util.List;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
import com.nttdata.bootcamp.s01accountservice.model.TransactionInput;
public interface AccountService {
/**
 * AccountService.
 * @param accountId busqueda por id.
 * @return un AccountDetails.
 */
	AccountDetails accountsAccountIdGet(String accountId);

	/**
	 * AccountService.
	 * @param requestBody el cuerpo.
	 * @return una lista AccountDetails.
	 */
	List<AccountDetails> accountsByListPost(List<String> requestBody);

	/**
	 * AccountService.
	 * @param accountCreateInput type.
	 * @return un AccountDetails.
	 */
	AccountDetails accountsPost(AccountCreateInput accountCreateInput);

	/**
	 * AccountService.
	 * @param requestBody lista del cuerpo.
	 * @param accountId variable.
	 * @return un AccountDetails.
	 */
	AccountDetails accountsAccountIdAddSignersPost(String accountId, List<String> requestBody);

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	AccountDetails accountsDepositPost(TransactionInput transactionInput);

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	AccountDetails accountsWithdrawPost(TransactionInput transactionInput);
}
