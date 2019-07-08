package residentevil.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import residentevil.domain.enums.Authority;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel {

    private UUID id;
    private String username;
    private Authority authority;
}
