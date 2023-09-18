package com.example.creditservice.maper;
import com.example.creditservice.document.CreditEntity;
import com.example.creditservice.model.Credit;
import java.math.BigDecimal;
public class MaperCredit {
    /**
     * Método para MaperCredit.
     * @param creditDto parametro de Credit.
     * @return CreditEntity.
     */
    public static CreditEntity dtotoCreditEntity(final Credit creditDto) {
        return CreditEntity.builder().id(creditDto.getId())
                .type(dtoCrediTypeType(creditDto.getType()))
                .id_cliente(creditDto.getIdCliente())
                .amount(creditDto.getAmount().intValue())
                .build();
    }

    /**
     * Método para MaperCredit.
     * @param typeEnum parametro de Credit.TypeEnum.
     * @return CreditEntity.CreditType.
     */
    private static CreditEntity.CreditType dtoCrediTypeType(final Credit.TypeEnum typeEnum) {
        if (typeEnum == null) {
            return null;
        }
        switch (typeEnum) {
            case PERSONAL:
                return CreditEntity.CreditType.PERSONAL;
            case EMPRESARIAL:
                return CreditEntity.CreditType.EMPRESARIAL;
            default:
                throw new IllegalArgumentException("Unexpected type: " + typeEnum);
        }
    }

    /**
     * Método para MaperCredit.
     * @param entity parametro de CreditEntity.
     * @return Credit.
     */
    public static Credit entityToDto(final CreditEntity entity) {
        return new Credit()
                .id(entity.getId())
                .type(entityCrediType(entity.getType()))
                .idCliente(entity.getId_cliente())
                .amount(BigDecimal.valueOf(entity.getAmount()));
    }

    /**
     * Método para MaperCredit.
     * @param creditType parametro de CreditEntity.CreditType.
     * @return Credit.TypeEnum.
     */
    private static Credit.TypeEnum entityCrediType(final CreditEntity.CreditType creditType) {
        if (creditType == null) {
            return null;
        }
        switch (creditType) {
            case PERSONAL:
                return Credit.TypeEnum.PERSONAL;
            case EMPRESARIAL:
                return Credit.TypeEnum.EMPRESARIAL;
            default:
                throw new IllegalArgumentException("Unexpected type: " + creditType);
        }
    }
}
