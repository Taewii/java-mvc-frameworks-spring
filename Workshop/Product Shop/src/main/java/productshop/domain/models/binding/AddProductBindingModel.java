package productshop.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddProductBindingModel {

    @NotBlank(message = "Please enter a name.")
    private String name;
    private String description;

    @NotNull(message = "Please enter a price.")
    @DecimalMin(value = "1", message = "Price must be positive.")
    private BigDecimal price;

    @NotEmpty(message = "Choose a category.")
    private List<Long> categories;

    private MultipartFile image;
}
