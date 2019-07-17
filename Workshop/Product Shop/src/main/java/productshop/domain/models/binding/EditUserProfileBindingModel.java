package productshop.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class EditUserProfileBindingModel {

    @NotBlank(message = "Please enter a password.")
    private String oldPassword;

    @NotBlank(message = "Please enter a password.")
    private String newPassword;

    @NotBlank(message = "Please enter a password.")
    private String newPasswordConfirm;

    @NotBlank(message = "Please enter a password.")
    @Email(message = "Please enter a valid email.")
    private String email;
}
