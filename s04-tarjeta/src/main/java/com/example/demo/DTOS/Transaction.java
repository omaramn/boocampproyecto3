package com.example.demo.DTOS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

/**
 * Clase que representa una entidad de Transaction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    /**
     * Identificador único de la Transaction.
     */
    private String id;

    /**
     * Identificador único de la clientId.
     */
    private String clientId;

    /**
     * Identificador único de la referenceId.
     */
    private String referenceId;

    /**
     * Identificador único de la amount.
     */
    private Double amount;

    /**
     * Identificador único de la description.
     */
    private String description;

    /**
     * Identificador único de la transactionDate.
     */
    private OffsetDateTime transactionDate;

    /**
     * Identificador único de la transactionType.
     */
    private TransactionType transactionType;

    /**
     * Identificador único de la TransactionType.
     */
    public enum TransactionType {
        /**
         * Identificador único de la CREDIT_CARD.
         */
        CREDIT_CARD,
        /**
         * Identificador único de la SAVINGS_ACCOUNT.
         */
        SAVINGS_ACCOUNT,
        /**
         * Identificador único de la LOAN.
         */
        LOAN
    }
}

