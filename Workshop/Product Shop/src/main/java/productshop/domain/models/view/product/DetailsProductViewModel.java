package productshop.domain.models.view.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.ProductModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DetailsProductViewModel implements ProductModel {

    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
