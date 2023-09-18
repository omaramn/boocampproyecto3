package com.example.creditservice.servicio;
import com.example.creditservice.document.CreditEntity;
import com.example.creditservice.maper.MaperCredit;
import com.example.creditservice.model.Credit;
import com.example.creditservice.repository.RepositoryCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ServiceCreditImpl  implements ServiceCredit {
    /**
     * Coneccion a la BD de RepositoryCredit.
     */
    @Autowired
    private RepositoryCredit repository;

    /**
     * AccountService.
     *
     * @param credit busqueda por Credit.
     * @return un Credit.
     */
    public Mono<ResponseEntity<Credit>>createCredit(Credit credit) {
        CreditEntity creditEntity = MaperCredit.dtotoCreditEntity(credit);
        return repository.save(creditEntity)
                .flatMap(e -> Mono.just(ResponseEntity.ok(MaperCredit.entityToDto(e))));
    }
}
