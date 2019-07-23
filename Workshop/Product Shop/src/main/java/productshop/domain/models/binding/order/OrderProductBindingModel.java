package productshop.domain.models.binding.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.OrderModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

import static productshop.config.Constants.NEGATIVE_ORDER_QUANTITY_MESSAGE;
import static productshop.config.Constants.NULL_QUANTITY_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductBindingModel implements OrderModel {

    @NotNull
    private UUID productId;

    private String customer;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    @NotNull(message = NULL_QUANTITY_MESSAGE)
    @Min(value = 1, message = NEGATIVE_ORDER_QUANTITY_MESSAGE)
    private Integer quantity;
}
