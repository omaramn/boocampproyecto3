package com.example.demo.mapper;
import com.example.demo.document.TransactionEntity;
import com.example.demo.model.Transaction;
import org.springframework.beans.BeanUtils;
import java.time.OffsetDateTime;
public class TransactionMapper {
    /**
     * Método para TransactionMapper.
     * @param model parametro de Transaction.
     * @return entity.
     */
    public static TransactionEntity toEntity(final Transaction model) {
        TransactionEntity entity = new TransactionEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setTransactionDate(model.getTransactionDate().toLocalDateTime());

        if (model.getSourceType() != null) {
            entity.setSourceType(ProductTypeMapper.toEntity(model.getSourceType()));
        }
        if (model.getDestinyType() != null) {
            entity.setDestinyType(ProductTypeMapper.toEntity(model.getDestinyType()));
        }
        if (model.getTransactionType() != null) {
            entity.setTransactionType(TransactionTypeMapper.toEntity(model.getTransactionType()));
        }
        return entity;
    }

    /**
     * Método para TransactionMapper.
     * @param entity parametro de TransactionEntity.
     * @return model.
     */
    public static Transaction toModel(final TransactionEntity entity) {
        Transaction model = new Transaction();
        BeanUtils.copyProperties(entity, model);
        model.setTransactionDate(entity.getTransactionDate().atOffset(OffsetDateTime.now().getOffset()));

        if (entity.getSourceType() != null) {
            model.setSourceType(ProductTypeMapper.toModel(entity.getSourceType()));
        }
        if (entity.getDestinyType() != null) {
            model.setDestinyType(ProductTypeMapper.toModel(entity.getDestinyType()));
        }
        if (entity.getTransactionType() != null) {
            model.setTransactionType(TransactionTypeMapper.toModel(entity.getTransactionType()));
        }
        return model;
    }
}

