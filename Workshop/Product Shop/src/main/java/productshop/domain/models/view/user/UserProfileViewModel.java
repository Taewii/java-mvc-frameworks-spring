package productshop.domain.models.view.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.UserModel;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileViewModel implements UserModel {

    private String email;
}
