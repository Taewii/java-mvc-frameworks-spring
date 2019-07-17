package productshop.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditUserProfileViewModel {

    //TODO: the password fields are not needed, but I don't know how to set the fields otherwise
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
    private String email;
}
