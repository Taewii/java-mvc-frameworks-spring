package productshop.domain.models.binding.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.config.Constants;
import productshop.domain.models.base.CategoryModel;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class AddCategoryBindingModel implements CategoryModel {

    @NotBlank(message = Constants.BLANK_USERNAME_MESSAGE)
    private String name;
}
