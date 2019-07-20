package productshop.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DeleteProductViewModel {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Set<Long> categories;
}
