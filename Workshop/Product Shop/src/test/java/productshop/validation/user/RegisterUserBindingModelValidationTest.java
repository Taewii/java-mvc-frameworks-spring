package productshop.validation.user;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.domain.models.binding.user.RegisterUserBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static productshop.config.Constants.*;

public class RegisterUserBindingModelValidationTest {

    private static final Map<String, String> requiredFields = new HashMap<>() {{
        put("username", BLANK_USERNAME_MESSAGE);
        put("password", BLANK_PASSWORD_MESSAGE);
        put("confirmPassword", BLANK_CONFIRM_PASSWORD_MESSAGE);
        put("email", BLANK_EMAIL_MESSAGE);
    }};

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    private void checkFields(Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations) {
        for (ConstraintViolation<RegisterUserBindingModel> violation : constraintViolations) {
            assertTrue(requiredFields.containsKey(violation.getPropertyPath().toString()));
            assertTrue(requiredFields.get(violation.getPropertyPath().toString()).equalsIgnoreCase(violation.getMessage()));
        }
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(4);
        checkFields(constraintViolations);
    }

    @Test
    public void withBlankParams_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername("");
        user.setPassword("");
        user.setConfirmPassword("");
        user.setEmail("");

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(4);
        checkFields(constraintViolations);
    }

    @Test
    public void withNullUsername_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername(null);
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<RegisterUserBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("username"));
    }

    @Test
    public void withNullPassword_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername("username");
        user.setPassword(null);
        user.setConfirmPassword("password");
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<RegisterUserBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("password"));
    }

    @Test
    public void withNullConfirmPassword_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword(null);
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<RegisterUserBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("confirmPassword");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("confirmPassword"));
    }

    @Test
    public void withNullEmail_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setEmail(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<RegisterUserBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("email"));
    }

    @Test
    public void withInvalidEmail_notValid_returnsCorrectMessages() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setEmail("invalid");

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<RegisterUserBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getMessage()).isEqualTo(INVALID_EMAIL_MESSAGE);
    }

    @Test
    public void withValidParameters_valid() {
        RegisterUserBindingModel user = new RegisterUserBindingModel();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setEmail("email@gmail.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<RegisterUserBindingModel>> constraintViolations = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }
}
