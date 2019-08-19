package productshop.validation.product;

import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import productshop.domain.models.binding.product.EditProductBindingModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static productshop.config.Constants.*;

public class EditProductBindingModelValidationTest {

    private static final Map<String, String> requiredFields = new HashMap<>() {{
        put("id", "must not be null");
        put("name", BLANK_USERNAME_MESSAGE);
        put("price", NULL_PRICE_MESSAGE);
        put("categories", EMPTY_CATEGORIES_MESSAGE);
    }};

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    private void checkFields(Set<ConstraintViolation<EditProductBindingModel>> constraintViolations) {
        for (ConstraintViolation<EditProductBindingModel> violation : constraintViolations) {
            assertTrue(requiredFields.containsKey(violation.getPropertyPath().toString()));
            assertTrue(requiredFields.get(violation.getPropertyPath().toString()).equalsIgnoreCase(violation.getMessage()));
        }
    }

    @Test
    public void withNullRequiredParams_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(4);
        checkFields(constraintViolations);
    }

    @Test
    public void withNullId_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(null);
        product.setName("name");
        product.setPrice(BigDecimal.TEN);
        product.setCategories(List.of(1L));

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("id");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("id"));
    }

    @Test
    public void withNullName_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName(null);
        product.setPrice(BigDecimal.TEN);
        product.setCategories(List.of(1L));

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("name"));
    }

    @Test
    public void withNullPrice_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName("name");
        product.setPrice(null);
        product.setCategories(List.of(1L));

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("price"));
    }

    @Test
    public void withNegativePrice_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName("name");
        product.setPrice(new BigDecimal(-1.2));
        product.setCategories(List.of(1L));

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getMessage()).isEqualTo(NEGATIVE_PRICE_MESSAGE);
    }

    @Test
    public void withZeroPrice_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName("name");
        product.setPrice(BigDecimal.ZERO);
        product.setCategories(List.of(1L));

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getMessage()).isEqualTo(NEGATIVE_PRICE_MESSAGE);
    }

    @Test
    public void withNullCategoryList_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName("name");
        product.setPrice(BigDecimal.TEN);
        product.setCategories(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("categories");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("categories"));
    }

    @Test
    public void withEmptyCategoryList_notValid_returnsCorrectMessages() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName("name");
        product.setPrice(BigDecimal.TEN);
        product.setCategories(new ArrayList<>());

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<EditProductBindingModel> violation = constraintViolations.iterator().next();

        assertThat(violation.getPropertyPath().toString()).isEqualTo("categories");
        assertThat(violation.getMessage()).isEqualTo(requiredFields.get("categories"));
    }

    @Test
    public void withValidParameters_valid() {
        EditProductBindingModel product = new EditProductBindingModel();
        product.setId(UUID.randomUUID());
        product.setName("name");
        product.setPrice(BigDecimal.TEN);
        product.setCategories(List.of(1L));

        Validator validator = createValidator();
        Set<ConstraintViolation<EditProductBindingModel>> constraintViolations = validator.validate(product);

        assertTrue(constraintViolations.isEmpty());
    }
}
