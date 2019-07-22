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
public class RegisterUserBindingModel implements UserModel {

    @NotBlank(message = BLANK_USERNAME_MESSAGE)
    private String username;

    @NotBlank(message = BLANK_PASSWORD_MESSAGE)
    private String password;

    @NotBlank(message = BLANK_CONFIRM_PASSWORD_MESSAGE)
    private String confirmPassword;

    @Email(message = INVALID_EMAIL_MESSAGE)
    @NotBlank(message = BLANK_EMAIL_MESSAGE)
    private String email;
}
