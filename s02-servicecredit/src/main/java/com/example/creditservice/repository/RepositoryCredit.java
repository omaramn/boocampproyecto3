package com.example.creditservice.repository;
import com.example.creditservice.document.CreditEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

/**
 * Interfaz que define un RepositoryCredit para CreditEntity.
 * */
@Component
public interface RepositoryCredit extends ReactiveMongoRepository<CreditEntity, String> {

}
