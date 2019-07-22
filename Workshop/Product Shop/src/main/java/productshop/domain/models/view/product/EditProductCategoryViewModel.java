package productshop.domain.models.view.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.ProductModel;

@Getter
@Setter
@NoArgsConstructor
public class EditProductCategoryViewModel implements ProductModel {

    private Long id;
}
