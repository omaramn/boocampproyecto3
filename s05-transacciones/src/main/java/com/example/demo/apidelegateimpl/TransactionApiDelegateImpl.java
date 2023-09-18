package com.example.demo.apidelegateimpl;
import com.example.demo.api.TransactionsApiDelegate;
import com.example.demo.document.TransactionEntity;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.SummaryResponse;
import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionApiDelegateImpl implements TransactionsApiDelegate {
    /**
     *  Llamada al TransactionService.
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * GET /transactions/client/{clientId} : Get transactions by client ID.
     *
     * @param clientId Client&#39;s ID. (required)
     * @return List of client&#39;s transactions. (status code 200)
     *         or Client not found. (status code 404)
     *         or Unexpected error. (status code 200)
     */
    @Override
    public Mono<ResponseEntity<Flux<Transaction>>> transactionsClientClientIdGet(String clientId, ServerWebExchange exchange) {

//        Flux<TransactionEntity> transactionFlux = transactionService.getTransactionsByClientId(clientId);
//        Flux<Transaction> apiModelTransactions = transactionFlux
//                .map(TransactionMapper::toModel);
//        ResponseEntity<Flux<Transaction>> responseEntity = ResponseEntity.ok(apiModelTransactions);
//        return Mono.just(responseEntity);

        return transactionService.getTransactionsByClientId(clientId)
                .map(TransactionMapper::toModel)
                .collectList()
                .flatMap(transactions -> {
                    if (!transactions.isEmpty()) {
                        return Mono.just(ResponseEntity.ok(Flux.fromIterable(transactions)));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    /**
     * POST /transactions : Register a new transaction.
     *
     * @param transaction  (required)
     * @return Transaction successfully created. (status code 201)
     *         or Unexpected error. (status code 200)
     */
    @Override
    public Mono<ResponseEntity<Transaction>> transactionsPost(Mono<Transaction> transaction, ServerWebExchange exchange) {
        return transaction
                .flatMap(apiModelTransaction -> {
                    TransactionEntity entityTransaction = TransactionMapper.toEntity(apiModelTransaction);
                    return transactionService.saveTransaction(entityTransaction)
                            .map(savedEntityTransaction -> {
                                Transaction savedApiModelTransaction = TransactionMapper.toModel(savedEntityTransaction);
                                if (savedApiModelTransaction != null) {
                                    return ResponseEntity.ok(savedApiModelTransaction);
                                } else {
                                    return ResponseEntity.unprocessableEntity().build();
                                }
                            });
                });
    }

    /**
     * GET /transactions/summary/daily/{clientId} : Get summary for a client&#39;s transactions.
     *
     * @param clientId Client&#39;s ID. (required)
     * @return Summary of client&#39;s transactions. (status code 200)
     *         or Client not found. (status code 404)
     *         or Unexpected error. (status code 200)
     */
    @Override
    public Mono<ResponseEntity<SummaryResponse>> transactionsSummaryDailyClientIdGet(String clientId, ServerWebExchange exchange) {
        return TransactionsApiDelegate.super.transactionsSummaryDailyClientIdGet(clientId, exchange);
    }
}
