    package com.example.demo.service;
    import com.example.demo.document.ClientEntity;
    import com.example.demo.document.TipoClienteEntity;
    import com.example.demo.document.PerfilEntity;
    import com.example.demo.mapper.ClientMapper;
    import com.example.demo.model.Client;
    import com.example.demo.repository.ClientRepository;
    import com.example.demo.repository.PerfilRepository;
    import com.example.demo.repository.TipoClienteRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.bson.types.ObjectId;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @Service
    public class ClientServiceImpl implements ClientService {

        /**
         * Para ClientRepository de la BD.
         */
        @Autowired
        private ClientRepository clientRepository;

        /**
         * Para TipoClienteRepository de la BD.
         */
        @Autowired
        private TipoClienteRepository tipoClienteRepository;

        /**
         * Para PerfilRepository de la BD.
         */
        @Autowired
        private PerfilRepository perfilRepository;

        /**
         * Para PerfilRepository de la BD.
         */
        private static final Map<String, String[]> clientTypeMap;

        static {
            clientTypeMap = new HashMap<>();
            clientTypeMap.put("1", new String[]{"personal", "vip"});
            clientTypeMap.put("2", new String[]{"empresarial", "pyme"});
        }

        /**
         * @param clientDto  (required)
         * @return Client created successfully (status code 201)
         *         or Bad request (status code 400)
         */
        public Mono<Client> createClient(final Client clientDto) {
            return Mono.just(clientDto.getTipoClienteId())
                    .map(clientTypeMap::get)
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("El valor del ID de TipoCliente no es válido.")))
                    .flatMap(clientType -> tipoClienteRepository.findByNombre(clientType[0])
                            .switchIfEmpty(createTipoClienteEntityById(clientType))
                    )
                    .flatMap(tipoClienteEntity  -> {
                        String tipoClienteIdAsString = tipoClienteEntity.getId().toString();
                        clientDto.setTipoClienteId(tipoClienteIdAsString);
                        ClientEntity entityToSave = ClientMapper.dtoToEntity(clientDto);
                        return clientRepository.save(entityToSave);
                    })
                    .map(ClientMapper::entityToDto);
        }

        /**
         * Método para guardar una transacción.
         * @param clientType variable String.
         * @return TipoClienteEntity.
         */
        public Mono<TipoClienteEntity> createTipoClienteEntityById(final String[] clientType) {
            return perfilRepository.findByNombre(clientType[1])
                    .switchIfEmpty(createPerfilEntity(clientType[1]))
                    .flatMap(perfilEntity -> {
                        TipoClienteEntity tipoClienteEntity = TipoClienteEntity.builder()
                                .nombre(clientType[0])
                                .perfilId(perfilEntity.getId())
                                .build();
                        return tipoClienteRepository.save(tipoClienteEntity);
                    });
        }

        /**
         * Método para guardar una transacción.
         * @param nombrePerfil variable String.
         * @return PerfilEntity.
         */
        public Mono<PerfilEntity> createPerfilEntity(final String nombrePerfil) {
            PerfilEntity newPerfilEntity = PerfilEntity.builder().nombre(nombrePerfil).build();
            return perfilRepository.save(newPerfilEntity);
        }

        /**
         *
         * @return List of clients retrieved successfully (status code 200)
         */
        public Flux<Client> getAllClients() {
            return clientRepository.findAll()
                    .map(ClientMapper::entityToDto);
        }

        /**
         *
         * @param id  (required)
         * @return Client details retrieved successfully (status code 200)
         *         or Client not found (status code 404)
         */
        public Mono<Client> getClientById(final String id) {
            return clientRepository.findById(id)
                    .map(ClientMapper::entityToDto);
        }

        /**
         *
         * @param idsFlux  (required)
         * @return Clients retrieved successfully (status code 200)
         *         or Bad request (status code 400)
         *         or Clients not found (status code 404)
         */
        public Flux<Client> bulkRetrieveClients(final Flux<String> idsFlux) {
            return idsFlux.collectList()
                    .flatMapMany(ids -> {
                        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());
                        return clientRepository.findAllByGivenIds(objectIds);
                    })
                    .map(ClientMapper::entityToDto);
        }
    }
