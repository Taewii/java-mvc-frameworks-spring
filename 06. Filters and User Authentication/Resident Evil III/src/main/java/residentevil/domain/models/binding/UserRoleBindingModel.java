package residentevil.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import residentevil.domain.enums.Authority;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleBindingModel {

    private UUID id;
    private Authority role;
}
