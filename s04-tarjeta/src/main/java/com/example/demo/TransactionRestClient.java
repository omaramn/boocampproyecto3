package com.example.demo;

import com.example.demo.DTOS.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class TransactionRestClient {

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public void sendTransaction(Transaction transaction) {
        try {
            // Configurar el ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            // Convertir y imprimir el objeto Transaction a JSON para depuración
            String transactionAsJson = objectMapper.writeValueAsString(transaction);
            System.out.println("Sending JSON: " + transactionAsJson);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Transaction> entity = new HttpEntity<>(transaction, headers);

            restTemplate.postForObject(transactionServiceUrl + "/transactions", entity, Void.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(); // Cambiado a e.printStackTrace() para una representación más clara
        }
    }
}