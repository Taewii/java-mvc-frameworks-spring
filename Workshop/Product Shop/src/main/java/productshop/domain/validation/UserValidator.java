package productshop.domain.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import productshop.domain.models.base.UserModel;
import productshop.domain.models.binding.user.EditUserProfileBindingModel;
import productshop.domain.models.binding.user.RegisterUserBindingModel;

import static productshop.config.Constants.BAD_REQUEST_ERROR_CODE;
import static productshop.config.Constants.PASSWORDS_DONT_MATCH_MESSAGE;

@Component
public class UserValidator implements Validator {

    private static final String PASSWORD_ATTRIBUTE = "password";
    private static final String CONFIRM_PASSWORD_ATTRIBUTE = "confirmPassword";
    private static final String NEW_PASSWORD_ATTRIBUTE = "newPassword";
    private static final String CONFIRM_NEW_PASSWORD_ATTRIBUTE = "newPasswordConfirm";

    @Override
    public boolean supports(Class<?> clazz) {
        return UserModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof EditUserProfileBindingModel) {
            EditUserProfileBindingModel profile = (EditUserProfileBindingModel) target;

            if (!profile.getNewPassword().equals(profile.getNewPasswordConfirm())) {
                errors.rejectValue(NEW_PASSWORD_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, PASSWORDS_DONT_MATCH_MESSAGE);
                errors.rejectValue(CONFIRM_NEW_PASSWORD_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, PASSWORDS_DONT_MATCH_MESSAGE);
            }
        } else if (target instanceof RegisterUserBindingModel) {
            RegisterUserBindingModel user = (RegisterUserBindingModel) target;

            if (!user.getPassword().equalsIgnoreCase(user.getConfirmPassword())) {
                errors.rejectValue(PASSWORD_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, PASSWORDS_DONT_MATCH_MESSAGE);
                errors.rejectValue(CONFIRM_PASSWORD_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, PASSWORDS_DONT_MATCH_MESSAGE);
            }
        }
    }
}

