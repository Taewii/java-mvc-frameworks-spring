package residentevil.domain.models.view;

import lombok.Data;

@Data
public class CapitalViewModel {

    private String name;

    //strings so they can keep the precision
    private String longitude;
    private String latitude;
}
