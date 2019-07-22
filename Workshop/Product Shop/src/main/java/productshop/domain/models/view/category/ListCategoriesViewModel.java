package productshop.domain.models.view.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.CategoryModel;

@Getter
@Setter
@NoArgsConstructor
public class ListCategoriesViewModel implements CategoryModel {

    private Long id;
    private String name;
}
