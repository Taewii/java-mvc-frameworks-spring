package residentevil.domain.models.view;

import lombok.Data;
import residentevil.domain.enums.Magnitude;
import residentevil.domain.enums.Mutation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class VirusViewModel {

    private UUID id;
    private String name;
    private String description;
    private String sideEffects;
    private String creator;
    private boolean IsDeadly;
    private boolean IsCurable;
    private Mutation mutation;
    private Byte turnoverRate;
    private Byte hoursUntilTurn;
    private Magnitude magnitude;
    private Date releasedOn;
    private Set<Long> affectedCapitals = new HashSet<>();
}
