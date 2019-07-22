package productshop.domain.models.binding.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.CategoryModel;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class DeleteCategoryBindingModel implements CategoryModel {

    @NotBlank
    private String name;
}
