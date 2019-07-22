package productshop.domain.models.binding.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import productshop.domain.models.base.ProductModel;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static productshop.config.Constants.*;

@Getter
@Setter
@NoArgsConstructor
public class AddProductBindingModel implements ProductModel {

    @NotBlank(message = BLANK_USERNAME_MESSAGE)
    private String name;
    private String description;

    @NotNull(message = NULL_PRICE_MESSAGE)
    @DecimalMin(value = MINIMUM_DECIMAL_PRICE_VALUE, message = NEGATIVE_PRICE_MESSAGE)
    private BigDecimal price;

    @NotEmpty(message = EMPTY_CATEGORIES_MESSAGE)
    private List<Long> categories;

    private MultipartFile image;
}
