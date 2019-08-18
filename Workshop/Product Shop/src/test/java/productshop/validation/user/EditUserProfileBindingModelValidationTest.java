package productshop.validation.user;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.domain.models.binding.user.EditUserProfileBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static productshop.config.Constants.*;

public class EditUserProfileBindingModelValidationTest {

    private static final Map<String, String> requiredFields = new HashMap<>() {{
        put("oldPassword", BLANK_PASSWORD_MESSAGE);
        put("newPassword", BLANK_PASSWORD_MESSAGE);
        put("newPasswordConfirm", BLANK_PASSWORD_MESSAGE);
        put("email", BLANK_EMAIL_MESSAGE);
    }};

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    private void checkFields(Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations) {
        for (ConstraintViolation<EditUserProfileBindingModel> violation : constraintViolations) {
            assertTrue(requiredFields.containsKey(violation.getPropertyPath().toString()));
            assertTrue(requiredFields.get(violation.getPropertyPath().toString()).equalsIgnoreCase(violation.getMessage()));
        }
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(4);
        checkFields(constraintViolations);
    }

    @Test
    public void withBlankParams_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword("");
        user.setNewPassword("");
        user.setNewPasswordConfirm("");
        user.setEmail("");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(4);
        checkFields(constraintViolations);
    }

    @Test
    public void withNullOldPassword_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword(null);
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirm("newPassword");
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditUserProfileBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("oldPassword");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("oldPassword"));
    }

    @Test
    public void withNullNewPassword_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword("oldPassword");
        user.setNewPassword(null);
        user.setNewPasswordConfirm("newPassword");
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditUserProfileBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("newPassword");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("newPassword"));
    }

    @Test
    public void withNullNewPasswordConfirmPassword_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword("oldPassword");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirm(null);
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditUserProfileBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("newPasswordConfirm");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("newPasswordConfirm"));
    }

    @Test
    public void withNullEmail_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword("oldPassword");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirm("newPassword");
        user.setEmail(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditUserProfileBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("email"));
    }

    @Test
    public void withInvalidEmail_notValid_returnsCorrectMessages() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword("oldPassword");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirm("newPassword");
        user.setEmail("invalid");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditUserProfileBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getMessage()).isEqualTo(INVALID_EMAIL_MESSAGE);
    }

    @Test
    public void withValidParameters_valid() {
        EditUserProfileBindingModel user = new EditUserProfileBindingModel();
        user.setOldPassword("oldPassword");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirm("newPassword");
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditUserProfileBindingModel>> constraintViolations = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }
}
