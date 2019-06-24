package realestateagency.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @Positive
    @Column(nullable = false)
    private BigDecimal apartmentRent;

    @NotBlank
    @Column(nullable = false)
    private String apartmentType;

    @PositiveOrZero
    @DecimalMax("100.00")
    @Column(nullable = false)
    private BigDecimal apartmentCommission;
}
