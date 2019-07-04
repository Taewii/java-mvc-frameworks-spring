package residentevil.domain.models.binding;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import residentevil.domain.enums.Magnitude;
import residentevil.domain.enums.Mutation;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class VirusBindingModel {

    private UUID id;

    @NotBlank
    @Size(min = 3, max = 10)
    private String name;

    @NotBlank
    @Size(min = 5, max = 100)
    private String description;

    @NotBlank
    @Size(max = 50)
    private String sideEffects;

    @NotBlank
    private String creator;

    private boolean IsDeadly;

    private boolean IsCurable;

    @NotNull
    private Mutation mutation;

    @Min(0)
    @Max(100)
    @NotNull
    private Integer turnoverRate;

    @Min(1)
    @Max(12)
    @NotNull
    private Integer hoursUntilTurn;

    private Magnitude magnitude;

    @Past
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releasedOn;

    @NotEmpty
    private Set<Long> affectedCapitals = new HashSet<>();
}
