package productshop.validation.product;

import org.junit.Test;
import org.springframework.validation.*;
import org.springframework.web.multipart.MultipartFile;
import productshop.domain.models.binding.product.AddProductBindingModel;
import productshop.domain.models.binding.product.DeleteProductBindingModel;
import productshop.domain.models.binding.product.EditProductBindingModel;
import productshop.domain.validation.ProductValidator;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static productshop.config.Constants.EMPTY_IMAGE_MESSAGE;

public class ProductValidatorTest {

    private static final Validator validator = new ProductValidator();

    @Test
    public void validatorSupportsNeededClasses() {
        assertTrue(validator.supports(AddProductBindingModel.class));
        assertTrue(validator.supports(EditProductBindingModel.class));
        assertTrue(validator.supports(DeleteProductBindingModel.class));
    }

    @Test
    public void addProductBindingModel_notValid_withEmptyImageField_returnsCorrectMessages() throws IOException {
        AddProductBindingModel product = new AddProductBindingModel();
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(true);
        product.setImage(multipartFile);
        Errors errors = new BeanPropertyBindingResult(product, "product");

        validator.validate(product, errors);
        assertEquals(1, errors.getErrorCount());

        List<ObjectError> violations = errors.getAllErrors();
        FieldError error = (FieldError) violations.get(0);

        assertEquals("image", error.getField());
        assertEquals(EMPTY_IMAGE_MESSAGE, error.getDefaultMessage());
    }

    @Test
    public void addProductBindingModel_valid() {
        AddProductBindingModel product = new AddProductBindingModel();
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(false);
        product.setImage(multipartFile);
        Errors errors = new BeanPropertyBindingResult(product, "product");

        validator.validate(product, errors);
        assertEquals(0, errors.getErrorCount());
    }
}
