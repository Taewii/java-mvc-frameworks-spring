package productshop.domain.models.view.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.OrderModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ListOrdersViewModel implements OrderModel {

    private String id;
    private String productImageUrl;
    private String customerUsername;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
}
