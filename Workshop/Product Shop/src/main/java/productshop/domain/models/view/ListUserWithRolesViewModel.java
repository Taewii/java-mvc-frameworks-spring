package productshop.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.enums.Authority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListUserWithRolesViewModel {

    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private Authority mainRole;
}
