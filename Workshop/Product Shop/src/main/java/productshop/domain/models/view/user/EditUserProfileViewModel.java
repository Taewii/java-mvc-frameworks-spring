package productshop.domain.models.view.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.UserModel;

@Getter
@Setter
@NoArgsConstructor
public class EditUserProfileViewModel implements UserModel {

    //TODO: the password fields are not needed, but I don't know how to set the fields otherwise
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
    private String email;
}
