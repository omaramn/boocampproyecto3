package com.example.creditservice.document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Clase que representa una entidad de CreditEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Credit")
public class CreditEntity {
    /**
     * Identificador único de la CreditEntity.
     */
    @Id
    private String id;

    /**
     * Identificador único de la type.
     */
    private CreditType type;

    /**
     * Identificador único de la id_cliente.
     */
    private String id_cliente;

    /**
     * Identificador único de la amount.
     */
    private Integer amount;

    /**
     * Identificador único de la creditType.
     */
    public enum CreditType {
        /**
         * Identificador único de la PERSONAL.
         */
        PERSONAL,
        /**
         * Identificador único de la EMPRESARIAL.
         */
        EMPRESARIAL
    }
}

