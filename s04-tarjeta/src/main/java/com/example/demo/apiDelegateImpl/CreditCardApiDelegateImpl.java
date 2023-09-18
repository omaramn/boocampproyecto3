package com.example.demo.apiDelegateImpl;

import com.example.demo.api.CreditCardsApi;
import com.example.demo.document.CreditCard;
import com.example.demo.Service.CreditCardService;
import com.example.demo.api.CreditCardsApiDelegate;
import com.example.demo.mapper.CreditCardMapper;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CreditCardApiDelegateImpl implements CreditCardsApiDelegate {
    /**
     * Para acceder a CreditCardService.
     */
    @Autowired
    private CreditCardService creditCardService;

    /**
     * POST /credit-cards : Assign a new credit card to a client (Create it).
     *
     * @param creditCardCreateInput  (required)
     * @return Credit card created successfully. (status code 201)
     *         or Bad request. Invalid input data. (status code 400)
     *         or Internal server error. (status code 500)
     * @see CreditCardsApi#creditCardsPost
     */
    @Override
    public Mono<ResponseEntity<CreditCardDetails>> creditCardsPost(Mono<CreditCardCreateInput> creditCardCreateInput, ServerWebExchange exchange) {

        return creditCardCreateInput.flatMap(creditCardCreateInputValue -> {
            CreditCard creditCard = CreditCardMapper.fromCreditCardCreateInput(creditCardCreateInputValue);
            return creditCardService.saveCreditCard(creditCard)
                    .map(savedCard -> ResponseEntity.ok(CreditCardMapper.toCreditCardDetails(savedCard)));
        });
    }

    /**
     * GET /credit-cards/{cardNumber}/balance : Check the balance and available credit limit of the card.
     *
     * @param cardNumber Credit card number. (required)
     * @return Balance retrieved successfully. (status code 200)
     *         or Credit card not found. (status code 404)
     *         or Internal server error. (status code 500)
     * @see CreditCardsApi#creditCardsCardNumberBalanceGet
     */
    @Override
    public Mono<ResponseEntity<CreditCardBalance>> creditCardsCardNumberBalanceGet(String cardNumber, ServerWebExchange exchange) {
        Mono<CreditCard> creditCardMono = creditCardService.findCardByNumber(cardNumber);

//        return creditCardMono.map(creditCard -> ResponseEntity.ok(CreditCardMapper.toCreditCardBalance(creditCard)))
//                .defaultIfEmpty(ResponseEntity.notFound().build());
        return creditCardService.findCardByNumber(cardNumber)
                .map(creditCard -> ResponseEntity.ok(CreditCardMapper.toCreditCardBalance(creditCard)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    /**
     * POST /credit-cards/{cardNumber}/payment : Make a payment towards the credit card balance.
     *
     * @param cardNumber Credit card number. (required)
     * @param creditCardPayment  (required)
     * @return Payment made successfully. (status code 200)
     *         or Invalid payment amount. (status code 400)
     *         or Credit card not found. (status code 404)
     *         or Internal server error. (status code 500)
     * @see CreditCardsApi#creditCardsCardNumberPaymentPost
     */
//    @Override
//    public ResponseEntity<Void> creditCardsCardNumberPaymentPost(final String cardNumber, final CreditCardPayment creditCardPayment) {
//        try {
//            creditCardService.makePayment(cardNumber, creditCardPayment.getAmount().doubleValue());
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
    @Override
    public Mono<ResponseEntity<Void>> creditCardsCardNumberPaymentPost(String cardNumber, Mono<CreditCardPayment> creditCardPayment, ServerWebExchange exchange) {
        return creditCardPayment
                .flatMap(payment -> {
                    try {
                        creditCardService.makePayment(cardNumber, payment.getAmount().doubleValue());
                        return Mono.just(ResponseEntity.ok().<Void>build());
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.badRequest().<Void>build());
                    }
                })
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().<Void>build()));
    }

    /**
     * POST /credit-cards/{cardNumber}/purchase : Register a purchase on the credit card.
     *
     * @param cardNumber Credit card number. (required)
     * @param creditCardPurchase  (required)
     * @return Purchase registered successfully. (status code 200)
     *         or Insufficient credit or invalid amount. (status code 400)
     *         or Credit card not found. (status code 404)
     *         or Internal server error. (status code 500)
     * @see CreditCardsApi#creditCardsCardNumberPurchasePost
     */
//    @Override
//    public ResponseEntity<Void> creditCardsCardNumberPurchasePost(final String cardNumber, final CreditCardPurchase creditCardPurchase) {
//        try {
//            creditCardService.registerPurchase(cardNumber, creditCardPurchase.getAmount().doubleValue());
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
    @Override
    public Mono<ResponseEntity<Void>> creditCardsCardNumberPurchasePost(String cardNumber, Mono<CreditCardPurchase> creditCardPurchase, ServerWebExchange exchange) {
        return CreditCardsApiDelegate.super.creditCardsCardNumberPurchasePost(cardNumber, creditCardPurchase, exchange);
    }



    /**
     * GET /credit-cards/by-client/{clientId} : Retrieve credit cards by client ID.
     *
     * @param clientId Client&#39;s ID. (required)
     * @return Credit cards retrieved successfully. (status code 200)
     *         or No credit cards found for the provided client ID. (status code 404)
     *         or Internal server error. (status code 500)
     * @see CreditCardsApi#creditCardsByClientClientIdGet
     */
//    @Override
//    public ResponseEntity<List<CreditCardDetails>> creditCardsByClientClientIdGet(final String clientId) {
//        List<CreditCard> creditCards = creditCardService.findCardsByClientId(clientId);
//        List<CreditCardDetails> creditCardDetailsList = creditCards.stream()
//                .map(CreditCardMapper::toCreditCardDetails)
//                .collect(Collectors.toList());
//        System.out.println("Returning: " + creditCardDetailsList);
//
//        return ResponseEntity.ok(creditCardDetailsList);
//    }
    @Override
    public Mono<ResponseEntity<Flux<CreditCardDetails>>> creditCardsByClientClientIdGet(String clientId, ServerWebExchange exchange) {
        return CreditCardsApiDelegate.super.creditCardsByClientClientIdGet(clientId, exchange);
    }
}
