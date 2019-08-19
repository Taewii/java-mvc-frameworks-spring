package productshop.validation.product;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.domain.models.binding.product.DeleteProductBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class DeleteProductBindingModelValidationTest {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    public void withNullId_notValid_returnsCorrectMessages() {
        DeleteProductBindingModel product = new DeleteProductBindingModel();
        product.setId(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<DeleteProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<DeleteProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("id");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    public void withValidParameters_valid() {
        DeleteProductBindingModel product = new DeleteProductBindingModel();
        product.setId(UUID.randomUUID());

        Validator validator = createValidator();
        Set<ConstraintViolation<DeleteProductBindingModel>> constraintViolations = validator.validate(product);

        assertTrue(constraintViolations.isEmpty());
    }
}
