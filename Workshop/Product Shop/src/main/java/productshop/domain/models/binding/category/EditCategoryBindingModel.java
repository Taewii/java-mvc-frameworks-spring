package productshop.domain.models.binding.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.config.Constants;
import productshop.domain.models.base.CategoryModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EditCategoryBindingModel implements CategoryModel {

    @NotNull
    private Long id;

    @NotBlank(message = Constants.BLANK_USERNAME_MESSAGE)
    private String name;
}
