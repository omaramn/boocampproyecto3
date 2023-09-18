package com.example.demo.service;
import com.example.demo.document.PerfilEntity;
import com.example.demo.document.TipoClienteEntity;
import com.example.demo.model.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ClientService {
    /**
     *
     * @param clientDto  (required)
     * @return Client created successfully (status code 201)
     *         or Bad request (status code 400)
     */
    Mono<Client> createClient(Client clientDto);

    /**
     * Método para guardar una transacción.
     * @param clientType variable String.
     * @return TipoClienteEntity.
     */
    Mono<TipoClienteEntity> createTipoClienteEntityById(String[] clientType);

    /**
     * Método para guardar una transacción.
     * @param nombrePerfil variable String.
     * @return PerfilEntity.
     */
    Mono<PerfilEntity> createPerfilEntity(String nombrePerfil);

    /**
     *
     * @return List of clients retrieved successfully (status code 200)
     */
    Flux<Client> getAllClients();

    /**
     *
     * @param id  (required)
     * @return Client details retrieved successfully (status code 200)
     *         or Client not found (status code 404)
     */
    Mono<Client> getClientById(String id);

    /**
     *
     * @param idsFlux  (required)
     * @return Clients retrieved successfully (status code 200)
     *         or Bad request (status code 400)
     *         or Clients not found (status code 404)
     */
    Flux<Client> bulkRetrieveClients(Flux<String> idsFlux);
}
