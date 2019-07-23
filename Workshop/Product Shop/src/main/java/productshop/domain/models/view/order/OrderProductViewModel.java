package productshop.domain.models.view.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.OrderModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductViewModel implements OrderModel {

    private String productId;
    private String customer;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity = 1;
}
