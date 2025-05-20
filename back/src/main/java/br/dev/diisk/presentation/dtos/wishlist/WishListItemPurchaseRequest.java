package br.dev.diisk.presentation.dtos.wishlist;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WishListItemPurchaseRequest {
    private Boolean isRecurring;
    private BigDecimal value;
    private Long creditCardId;

    private String startDate;
    private BigDecimal totalValue;
    private Integer installmentsCount;
    private String endDate;
    private String dueDay;
    private String recurringDay;


    private String paymentDate;

}
