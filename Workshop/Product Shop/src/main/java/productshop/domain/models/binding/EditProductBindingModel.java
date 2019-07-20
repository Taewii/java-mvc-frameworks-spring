package productshop.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EditProductBindingModel {

    @NotNull
    private UUID id;

    @NotBlank(message = "Please enter a name.")
    private String name;
    private String description;

    @NotNull(message = "Please enter a price.")
    @DecimalMin(value = "1", message = "Price must be positive.")
    private BigDecimal price;

    @NotEmpty(message = "Choose a category.")
    private List<Long> categories;
}
