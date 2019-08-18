package productshop.validation.category;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.config.Constants;
import productshop.domain.models.binding.category.DeleteCategoryBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class DeleteCategoryBindingModelValidationTest {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        DeleteCategoryBindingModel category = new DeleteCategoryBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<DeleteCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<DeleteCategoryBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    public void withValidParameters_valid() {
        DeleteCategoryBindingModel category = new DeleteCategoryBindingModel();
        category.setName("name");

        Validator validator = createValidator();
        Set<ConstraintViolation<DeleteCategoryBindingModel>> constraintViolations = validator.validate(category);

        assertTrue(constraintViolations.isEmpty());
    }
}
