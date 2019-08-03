package productshop.domain.models.view.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.OrderModel;

@Getter
@Setter
@NoArgsConstructor
public class CartViewOrderModel implements OrderModel {

    private String productName;
    private Integer quantity;
}
