package productshop.domain.models.binding.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import productshop.domain.models.base.UserModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static productshop.config.Constants.*;

@Getter
@Setter
@NoArgsConstructor
public class EditUserProfileBindingModel implements UserModel {

    @NotBlank(message = BLANK_PASSWORD_MESSAGE)
    private String oldPassword;

    @NotBlank(message = BLANK_PASSWORD_MESSAGE)
    private String newPassword;

    @NotBlank(message = BLANK_PASSWORD_MESSAGE)
    private String newPasswordConfirm;

    @NotBlank(message = BLANK_EMAIL_MESSAGE)
    @Email(message = INVALID_EMAIL_MESSAGE)
    private String email;
}
