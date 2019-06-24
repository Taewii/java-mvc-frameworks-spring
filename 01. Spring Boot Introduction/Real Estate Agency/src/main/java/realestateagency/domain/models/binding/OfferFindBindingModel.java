package realestateagency.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OfferFindBindingModel {

    @Positive
    @NotNull
    private BigDecimal familyBudget;

    @NotBlank
    private String familyApartmentType;

    @NotBlank
    private String familyName;
}
