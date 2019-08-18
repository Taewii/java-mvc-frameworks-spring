package productshop.validation.order;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.domain.models.binding.order.OrderProductBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static productshop.config.Constants.NEGATIVE_ORDER_QUANTITY_MESSAGE;
import static productshop.config.Constants.NULL_QUANTITY_MESSAGE;

public class OrderProductBindingModelValidationTest {

    private static final Map<String, String> requiredFields = new HashMap<>() {{
        put("productId", "must not be null");
        put("quantity", NULL_QUANTITY_MESSAGE);
    }};

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    private void checkFields(Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations) {
        for (ConstraintViolation<OrderProductBindingModel> violation : constraintViolations) {
            assertTrue(requiredFields.containsKey(violation.getPropertyPath().toString()));
            assertTrue(requiredFields.get(violation.getPropertyPath().toString()).equalsIgnoreCase(violation.getMessage()));
        }
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        OrderProductBindingModel order = new OrderProductBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(2);
        checkFields(constraintViolations);
    }

    @Test
    public void withNullProductId_notValid_returnsCorrectMessages() {
        OrderProductBindingModel order = new OrderProductBindingModel();
        order.setQuantity(10);

        Validator validator = createValidator();
        Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<OrderProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("productId");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    public void withNullQuantity_notValid_returnsCorrectMessages() {
        OrderProductBindingModel order = new OrderProductBindingModel();
        order.setProductId(UUID.randomUUID());

        Validator validator = createValidator();
        Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<OrderProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getMessage()).isEqualTo(NULL_QUANTITY_MESSAGE);
    }

    @Test
    public void withNegativeQuantity_notValid_returnsCorrectMessages() {
        OrderProductBindingModel order = new OrderProductBindingModel();
        order.setProductId(UUID.randomUUID());
        order.setQuantity(-1);

        Validator validator = createValidator();
        Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<OrderProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getMessage()).isEqualTo(NEGATIVE_ORDER_QUANTITY_MESSAGE);
    }

    @Test
    public void withZeroQuantity_notValid_returnsCorrectMessages() {
        OrderProductBindingModel order = new OrderProductBindingModel();
        order.setProductId(UUID.randomUUID());
        order.setQuantity(0);

        Validator validator = createValidator();
        Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations = validator.validate(order);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<OrderProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getMessage()).isEqualTo(NEGATIVE_ORDER_QUANTITY_MESSAGE);
    }

    @Test
    public void withValidParameters_valid() {
        OrderProductBindingModel order = new OrderProductBindingModel();
        order.setProductId(UUID.randomUUID());
        order.setQuantity(1);

        Validator validator = createValidator();
        Set<ConstraintViolation<OrderProductBindingModel>> constraintViolations = validator.validate(order);

        assertTrue(constraintViolations.isEmpty());
    }
}
