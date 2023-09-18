package com.example.demo.apidelegateImpl;
import com.example.demo.service.ClientService;
import com.example.demo.api.ClientsApiDelegate;
import com.example.demo.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ClientApiDelegateImpl implements ClientsApiDelegate {
    /**
     * Para acceder a clientService.
     */
    @Autowired
    private ClientService clientService;

    /**
     * Método para guardar una transacción.
     * @return ClientsApiDelegate.
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ClientsApiDelegate.super.getRequest();
    }

    /**
     *
     * @param clientMono  (required)
     * @return Client created successfully (status code 201)
     *         or Bad request (status code 400)
     */
    @Override
    public Mono<ResponseEntity<Void>> createClient(final Mono<Client> clientMono, final ServerWebExchange exchange) {
        return clientMono
                .flatMap(clientService::createClient)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED)))
                .onErrorReturn(new ResponseEntity<Void>(HttpStatus.BAD_REQUEST));
    }

    /**
     *
     * @return List of clients retrieved successfully (status code 200)
     */
    @Override
    public Mono<ResponseEntity<Flux<Client>>> getAllClients(final ServerWebExchange exchange) {
        Flux<Client> clientsFlux = clientService.getAllClients();
        return Mono.just(ResponseEntity.ok(clientsFlux));
    }

    /**
     * @param clientId  (required)
     * @return Client details retrieved successfully (status code 200)
     *         or Client not found (status code 404)
     */
    @Override
    public Mono<ResponseEntity<Client>> getClientById(final String clientId, final ServerWebExchange exchange) {
        Mono<Client> clientMono = clientService.getClientById(clientId);
        return clientMono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    /**
     *
     * @param idsFlux  (required)
     * @return Clients retrieved successfully (status code 200)
     *         or Bad request (status code 400)
     *         or Clients not found (status code 404)
     */
    @Override
    public Mono<ResponseEntity<Flux<Client>>> bulkRetrieveClients(final Flux<String> idsFlux, final ServerWebExchange exchange) {
        Flux<Client> clientsFlux = clientService.bulkRetrieveClients(idsFlux);
        return Mono.just(ResponseEntity.ok(clientsFlux));
    }
}
