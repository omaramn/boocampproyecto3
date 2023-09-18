package com.example.creditservice.apidelegateimpl;
import com.example.creditservice.api.CreditsApiDelegate;
import com.example.creditservice.model.Credit;
import com.example.creditservice.servicio.ServiceCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CreditApiDelagateImpl implements CreditsApiDelegate {
    /**
     * Para acceder a ServiceCredit.
     */
    @Autowired
    private ServiceCredit serviceCredit;

    /**
     * Método para guardar una transacción.
     * @param credit parametro de Credit.
     * @return Credit del credit.
     */
    @Override
    public Mono<ResponseEntity<Credit>> applyForCredit(final Mono<Credit> credit, ServerWebExchange exchange) {
        //return  Mono.just(credit.flatMap((Credit creditMono) -> serviceCredit.createCredit(creditMono)));
        return credit
        .flatMap((Credit creditMono) -> serviceCredit.createCredit(creditMono));
        //return Mono.just(ResponseEntity.ok(new Credit()));
    }
}
