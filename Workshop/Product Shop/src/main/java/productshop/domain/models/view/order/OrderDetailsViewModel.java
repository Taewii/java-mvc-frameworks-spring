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
public class OrderDetailsViewModel implements OrderModel {

    private String productName;
    private String productDescription;
    private String customerUsername;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private String productImageUrl;
}
