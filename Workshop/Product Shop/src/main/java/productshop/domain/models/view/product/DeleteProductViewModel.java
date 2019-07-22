package productshop.domain.models.view.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.ProductModel;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DeleteProductViewModel implements ProductModel {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Set<Long> categories;
}
