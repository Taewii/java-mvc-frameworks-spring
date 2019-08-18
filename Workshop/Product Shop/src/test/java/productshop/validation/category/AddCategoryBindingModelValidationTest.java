package productshop.validation.category;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.config.Constants;
import productshop.domain.models.binding.category.AddCategoryBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class AddCategoryBindingModelValidationTest {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        AddCategoryBindingModel category = new AddCategoryBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<AddCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AddCategoryBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo(Constants.BLANK_USERNAME_MESSAGE);
    }

    @Test
    public void withBlankParams_notValid_returnsCorrectMessages() {
        AddCategoryBindingModel category = new AddCategoryBindingModel();
        category.setName("");

        Validator validator = createValidator();
        Set<ConstraintViolation<AddCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AddCategoryBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo(Constants.BLANK_USERNAME_MESSAGE);
    }

    @Test
    public void withValidParameters_valid() {
        AddCategoryBindingModel category = new AddCategoryBindingModel();
        category.setName("name");

        Validator validator = createValidator();
        Set<ConstraintViolation<AddCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertTrue(constraintViolations.isEmpty());
    }
}
