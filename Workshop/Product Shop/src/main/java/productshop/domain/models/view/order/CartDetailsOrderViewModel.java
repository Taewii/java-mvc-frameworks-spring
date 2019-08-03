package productshop.domain.models.view.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.OrderModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CartDetailsOrderViewModel implements OrderModel {

    private String id;
    private String productImageUrl;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer quantity;
}
