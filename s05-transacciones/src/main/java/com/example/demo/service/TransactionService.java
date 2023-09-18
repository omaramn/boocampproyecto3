package com.example.demo.service;
import com.example.demo.document.TransactionEntity;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TransactionService {
    /**
     * Repositorio para acceder a la BD de TransactionRepository.
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Método para guardar una transacción.
     *
     * @param transaction El objeto transacción (debe ser final).
     * @return Retorna la transacción guardada.
     */
    public Mono<TransactionEntity> saveTransaction(TransactionEntity transaction){
        return transactionRepository.save(transaction);
    }

    /**
     * Buscar por ClientId.
     *
     * @param clientId Variable de cliente.
     * @return RettransactionRepositoryorna del clientId.
     */
    public Flux<TransactionEntity> getTransactionsByClientId(final String clientId) {
        return  transactionRepository.findByClientId(clientId);
    }

    /**
     * Buscar por ClientId.
     * @param clientId Variable de cliente.
     * @return summaryResponse para SummaryResponse.
     */
//    public SummaryResponse transactionsSummaryDailyClientIdGet(final String clientId) {
//        LocalDateTime currentDate = LocalDateTime.now();
//        int currentMonth = currentDate.getMonthValue();
//        int currentYear = currentDate.getYear();
//        //Comunicacion de microservicios
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8081/accounts/byList";
//        List<String> clientIds = Arrays.asList(clientId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(clientIds, headers);
//        ParameterizedTypeReference<List<AccountDetails>> responseType = new ParameterizedTypeReference<List<AccountDetails>>() {
//        };
//        ResponseEntity<List<AccountDetails>> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                responseType
//        );
//        List<AccountDetails> accounts = response.getBody();
//        //fin de Comunicacion de microservicios
//
//        SummaryResponse summaryResponse = new SummaryResponse();
//        summaryResponse.setClientId(clientId);
//        summaryResponse.setSummaries(accounts.stream().map(account -> {
//            List<Transaction> currentMonthTransactions = this.getTransactionsByClientId(clientId)// and accoumm
//                    .stream()
//                    .filter(transaction -> transaction.getTransactionDate().getMonthValue() == currentMonth
//                            && transaction.getTransactionDate().getYear() == currentYear)
//                    .map(TransactionMapper::toModel)
//                    .collect(Collectors.toList());
//
//            Map<OffsetDateTime, Double> dailyBalances = currentMonthTransactions.stream()
//                    .collect(Collectors.groupingBy(
//                            transaction -> transaction.getTransactionDate(),
//                            Collectors.summingDouble(Transaction::getAmount)));
//
//            double averageDailyBalance = dailyBalances.values()
//                    .stream()
//                    .mapToDouble(balance -> balance)
//                    .average()
//                    .orElse(0.0);
//
//            Summary summary = new Summary();
//            summary.setAccountId(account.getId());
//            summary.setAverageDailyBalance(averageDailyBalance);
//            return summary;
//        }).collect(Collectors.toList()));
//        return summaryResponse;
//    }
}

