package residentevil.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapitalViewModel {

    private String name;

    //strings so they can keep the precision
    private String longitude;
    private String latitude;
}
