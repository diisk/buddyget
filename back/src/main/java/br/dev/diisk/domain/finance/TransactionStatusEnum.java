package br.dev.diisk.domain.finance;

import br.dev.diisk.domain.shared.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
public enum TransactionStatusEnum implements IBaseEnum {
    PENDING("Pendente"),
    PAID("Pago"),
    LATE("Atrasado"),
    RECEIVED("Recebido");

    private final String description;

    TransactionStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
