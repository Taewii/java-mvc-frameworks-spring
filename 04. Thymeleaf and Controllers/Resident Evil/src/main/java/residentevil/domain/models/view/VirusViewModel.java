package residentevil.domain.models.view;

import lombok.Data;
import residentevil.domain.enums.Magnitude;

import java.util.Date;
import java.util.UUID;

@Data
public class VirusViewModel {

    private UUID id;
    private String name;
    private Magnitude magnitude;
    private Date releasedOn;
}
