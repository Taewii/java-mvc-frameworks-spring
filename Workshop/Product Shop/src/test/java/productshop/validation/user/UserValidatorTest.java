package productshop.validation.user;

import org.junit.Test;
import org.springframework.validation.*;
import productshop.domain.models.binding.user.EditUserProfileBindingModel;
import productshop.domain.models.binding.user.RegisterUserBindingModel;
import productshop.domain.validation.UserValidator;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static productshop.config.Constants.PASSWORDS_DONT_MATCH_MESSAGE;

public class UserValidatorTest {

    private static final Validator validator = new UserValidator();

    @Test
    public void validatorSupportsNeededClasses() {
        assertTrue(validator.supports(RegisterUserBindingModel.class));
        assertTrue(validator.supports(EditUserProfileBindingModel.class));
    }

    @Test
    public void registerUserBindingModel_notValid_withNotMatchingPasswords_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setPassword("password");
        user.setConfirmPassword("otherPassword");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);
        assertEquals(2, errors.getErrorCount());

        List<ObjectError> violations = errors.getAllErrors();
        FieldError error = (FieldError) violations.get(0);

        assertEquals("password", error.getField());
        assertEquals(PASSWORDS_DONT_MATCH_MESSAGE, error.getDefaultMessage());

        error = (FieldError) violations.get(1);
        assertEquals("confirmPassword", error.getField());
        assertEquals(PASSWORDS_DONT_MATCH_MESSAGE, error.getDefaultMessage());
    }

    @Test
    public void registerUserBindingModel_valid_withMatchingPasswords() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setPassword("password");
        user.setConfirmPassword("password");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);
        assertEquals(0, errors.getErrorCount());
    }

    @Test
    public void editUserProfileBindingModel_notValid_withNotMatchingPasswords_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setNewPassword("password");
        user.setNewPasswordConfirm("otherPassword");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);
        assertEquals(2, errors.getErrorCount());

        List<ObjectError> violations = errors.getAllErrors();
        FieldError error = (FieldError) violations.get(0);

        assertEquals("newPassword", error.getField());
        assertEquals(PASSWORDS_DONT_MATCH_MESSAGE, error.getDefaultMessage());

        error = (FieldError) violations.get(1);
        assertEquals("newPasswordConfirm", error.getField());
        assertEquals(PASSWORDS_DONT_MATCH_MESSAGE, error.getDefaultMessage());
    }

    @Test
    public void editUserProfileBindingModel_valid_withMatchingPasswords() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setNewPassword("password");
        user.setNewPasswordConfirm("password");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);
        assertEquals(0, errors.getErrorCount());
    }
}
