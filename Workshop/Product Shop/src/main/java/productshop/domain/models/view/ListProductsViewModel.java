package productshop.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ListProductsViewModel {

    private String id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
