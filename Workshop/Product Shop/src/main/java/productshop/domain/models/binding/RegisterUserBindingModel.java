package productshop.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserBindingModel {

    @NotBlank(message = "Please enter a username.")
    private String username;

    @NotBlank(message = "Please enter a password.")
    private String password;

    @NotBlank(message = "Please re-enter your password.")
    private String confirmPassword;

    @Email(message = "Please enter a valid email.")
    @NotBlank(message = "Please enter an email.")
    private String email;
}
