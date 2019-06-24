package realestateagency.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OfferRegisterBindingModel {

    @Positive
    @NotNull
    private BigDecimal apartmentRent;

    @NotBlank
    private String apartmentType;

    @NotNull
    @PositiveOrZero
    @DecimalMax("100.00")
    private BigDecimal apartmentCommission;
}
