package productshop.validation.category;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.config.Constants;
import productshop.domain.models.binding.category.EditCategoryBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class EditCategoryBindingModelValidationTest {

    private static final Map<String, String> requiredFields = new HashMap<>() {{
        put("id", "must not be null");
        put("name", Constants.BLANK_USERNAME_MESSAGE);
    }};

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    private void checkFields(Set<ConstraintViolation<EditCategoryBindingModel>> constraintViolations) {
        for (ConstraintViolation<EditCategoryBindingModel> violation : constraintViolations) {
            assertTrue(requiredFields.containsKey(violation.getPropertyPath().toString()));
            assertTrue(requiredFields.get(violation.getPropertyPath().toString()).equalsIgnoreCase(violation.getMessage()));
        }
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        EditCategoryBindingModel category = new EditCategoryBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<EditCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertThat(constraintViolations.size()).isEqualTo(2);
        checkFields(constraintViolations);
    }

    @Test
    public void withNullId_notValid_returnsCorrectMessages() {
        EditCategoryBindingModel order = new EditCategoryBindingModel();
        order.setName("name");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditCategoryBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditCategoryBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("id");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    public void withNullName_notValid_returnsCorrectMessages() {
        EditCategoryBindingModel order = new EditCategoryBindingModel();
        order.setId(1L);

        Validator validator = createValidator();
        Set<ConstraintViolation<EditCategoryBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditCategoryBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo(Constants.BLANK_USERNAME_MESSAGE);
    }

    @Test
    public void withBlankName_notValid_returnsCorrectMessages() {
        EditCategoryBindingModel order = new EditCategoryBindingModel();
        order.setId(1L);
        order.setName("");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditCategoryBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditCategoryBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo(Constants.BLANK_USERNAME_MESSAGE);
    }

    @Test
    public void withValidParameters_valid() {
        EditCategoryBindingModel category = new EditCategoryBindingModel();
        category.setId(1L);
        category.setName("name");

        Validator validator = createValidator();
        Set<ConstraintViolation<EditCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertTrue(constraintViolations.isEmpty());
    }
}
