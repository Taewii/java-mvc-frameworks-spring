package productshop.domain.models.binding.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.ProductModel;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DeleteProductBindingModel implements ProductModel {

    @NotNull
    UUID id;
}
