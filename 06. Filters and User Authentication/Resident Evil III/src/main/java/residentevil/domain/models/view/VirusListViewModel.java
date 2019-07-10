package residentevil.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import residentevil.domain.enums.Magnitude;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VirusListViewModel {

    private UUID id;
    private String name;
    private Magnitude magnitude;
    private Date releasedOn;
}
