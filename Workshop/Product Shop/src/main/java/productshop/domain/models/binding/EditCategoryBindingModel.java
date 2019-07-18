package productshop.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EditCategoryBindingModel {

    @NotNull
    private Long id;

    @NotBlank(message = "Please enter a name.")
    private String name;
}
