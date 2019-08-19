package productshop.domain.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import productshop.domain.models.base.ProductModel;
import productshop.domain.models.binding.product.AddProductBindingModel;

import static productshop.config.Constants.BAD_REQUEST_ERROR_CODE;
import static productshop.config.Constants.EMPTY_IMAGE_MESSAGE;

@Component
public class ProductValidator implements Validator {

    private static final String IMAGE_ATTRIBUTE = "image";

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof AddProductBindingModel) {
            AddProductBindingModel product = (AddProductBindingModel) target;

            if (product.getImage().isEmpty()) {
                errors.rejectValue(IMAGE_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, EMPTY_IMAGE_MESSAGE);
            }
        }
    }
}
