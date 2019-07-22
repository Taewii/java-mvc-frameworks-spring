package productshop.domain.models.view.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.enums.Authority;
import productshop.domain.models.base.UserModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListUserWithRolesViewModel implements UserModel {

    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private Authority mainRole;
}
