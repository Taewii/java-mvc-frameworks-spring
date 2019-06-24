package realestateagency.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OfferHomeViewModel {

    private BigDecimal apartmentRent;
    private String apartmentType;
    private BigDecimal apartmentCommission;
}
