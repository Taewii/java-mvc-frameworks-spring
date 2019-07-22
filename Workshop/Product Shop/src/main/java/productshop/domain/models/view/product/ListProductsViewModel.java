package productshop.domain.models.view.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.ProductModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ListProductsViewModel implements ProductModel {

    private String id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
