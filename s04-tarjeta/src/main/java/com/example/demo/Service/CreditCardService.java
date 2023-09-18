package com.example.demo.Service;
import com.example.demo.document.CreditCard;
import com.example.demo.Repository.CreditCardRepository;
import com.example.demo.TransactionRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.DTOS.Transaction;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
@Service
public class CreditCardService {
    /**
     * Coneccion de la BD CreditCardRepository.
     */
    @Autowired
    private CreditCardRepository creditCardRepository;
    /**
     * injeccion de TransactionRestClient.
     */
    @Autowired
    private TransactionRestClient transactionRestClient;

    /**
     * injeccion de SecureRandom random.
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * CreditCardService.
     * @param creditCard de CreditCard.
     * @return CreditCard.
     */
    public Mono<CreditCard> saveCreditCard(final CreditCard creditCard) {

        if (creditCard.getCardNumber() == null || creditCard.getCardNumber().isEmpty()) {
            creditCard.setCardNumber(generateUniqueCardNumber());
        }
        return creditCardRepository.save(creditCard);
    }
//    /**
//     * CreditCardService.
//     * @param cardNumber variable.
//     * @return CreditCard.
//     */
//    public CreditCard findCardByNumber(final String cardNumber) {
//        return creditCardRepository.findByCardNumber(cardNumber).block();
//    }
        public Mono<CreditCard> findCardByNumber(final String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }


    /**
     * CreditCardService.
     * @param cardNumber variable.
     * @param amount variable.
     */
    public Mono<Void> makePayment(final String cardNumber, final Double amount) {
        return findCardByNumber(cardNumber)
                .flatMap(creditCard -> {
                    if (creditCard != null) {
                        double newBalance = creditCard.getBalance() - amount;
                        double newAvailableCredit = creditCard.getLimit() - newBalance;
                        creditCard.setBalance(newBalance);
                        creditCard.setAvailableCredit(newAvailableCredit);
                        return saveCreditCard(creditCard)
                                .then()
                                .then();
                    } else {
                        return Mono.empty(); // Tarjeta no encontrada
                    }
                });
    }

//    /**
//     * CreditCardService.
//     * @param cardNumber variable.
//     * @param amount variable.
//     */
//    public void registerPurchase(final String cardNumber, final Double amount) {
//        CreditCard creditCard = findCardByNumber(cardNumber);
//        if (creditCard != null && creditCard.getAvailableCredit() >= amount) {
//            creditCard.setBalance(creditCard.getBalance() + amount);
//            creditCard.setAvailableCredit(creditCard.getLimit() - creditCard.getBalance());
//            saveCreditCard(creditCard);
//            sendTransactionToService(creditCard.getClientId(), "CREDIT_CARD", "Purchase", amount);
//        }
//    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateRandomCardNumber();
        } while (isCardNumberExist(cardNumber));
        return cardNumber;
    }

    private String generateRandomCardNumber() {
        StringBuilder generatedNumber = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int randomNum = random.nextInt(10);
            generatedNumber.append(randomNum);
        }
        return generatedNumber.toString();
    }

    /**
     * CreditCardService.
     * @param cardNumber variable.
     * @return creditCardRepository.
     */
    private boolean isCardNumberExist(final String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber) != null;
    }

    /**
     * CreditCardService.
     * @param clientId variable.
     * @param type variable.
     * @param description variable.
     * @param amount variable.
     */
    private void sendTransactionToService(final String clientId, final String type, final String description, final Double amount) {
        try {
            Transaction transaction = new Transaction();
            transaction.setId("999999");
            transaction.setClientId(clientId);
            transaction.setTransactionType(Transaction.TransactionType.valueOf(type));
            transaction.setReferenceId(description);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setTransactionDate(OffsetDateTime.parse("2023-08-30T21:43:45.983+00:00"));
            transactionRestClient.sendTransaction(transaction);
        } catch(Exception e) {

        }
    }

    /**
     * CreditCardService.
     * @param clientId variable.
     * @return CreditCard.
     */
//    public List<CreditCard> findCardsByClientId(final String clientId) {
//        return creditCardRepository.findByClientId(clientId).collectList().block();
//    }
}


