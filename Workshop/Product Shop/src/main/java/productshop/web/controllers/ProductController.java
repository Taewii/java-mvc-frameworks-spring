package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import productshop.domain.models.binding.product.AddProductBindingModel;
import productshop.domain.models.binding.product.DeleteProductBindingModel;
import productshop.domain.models.binding.product.EditProductBindingModel;
import productshop.domain.models.view.category.ListCategoriesViewModel;
import productshop.domain.models.view.product.DeleteProductViewModel;
import productshop.domain.models.view.product.DetailsProductViewModel;
import productshop.domain.models.view.product.EditProductViewModel;
import productshop.domain.validation.ProductValidator;
import productshop.services.CategoryService;
import productshop.services.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static productshop.config.Constants.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final String PRODUCT_VIEWS_PATH = "product";
    private static final String ALL_VIEW = PRODUCT_VIEWS_PATH + "/" + "all";
    private static final String ADD_VIEW = PRODUCT_VIEWS_PATH + "/" + "add";
    private static final String EDIT_VIEW = PRODUCT_VIEWS_PATH + "/" + "edit";
    private static final String DELETE_VIEW = PRODUCT_VIEWS_PATH + "/" + "delete";

    private static final String CATEGORIES_ATTRIBUTE = "categories";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String IMAGE_ATTRIBUTE = "image";
    private static final String ID_ATTRIBUTE = "id";

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductValidator productValidator;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             ProductValidator productValidator) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productValidator = productValidator;
    }

    @ModelAttribute(CATEGORIES_ATTRIBUTE)
    public List<ListCategoriesViewModel> categories() {
        return categoryService.findAll();
    }

    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute(PRODUCTS_ATTRIBUTE, productService.findAll());
        return ALL_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details/{id}")
    public String details(@PathVariable(ID_ATTRIBUTE) UUID id, Model model) {
        model.addAttribute("product", productService.findById(id, DetailsProductViewModel.class));
        return "product/details";
    }

    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute(PRODUCT_ATTRIBUTE, new AddProductBindingModel());
        return ADD_VIEW;
    }

    @PreAuthorize(IS_MODERATOR)
    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute(PRODUCT_ATTRIBUTE) AddProductBindingModel product, Errors errors) {
        if (errors.hasErrors()) {
            return ADD_VIEW;
        }

        String productId = productService.add(product);
        if (productId == null) {
            errors.rejectValue(IMAGE_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, INVALID_IMAGE_MESSAGE);
            return ADD_VIEW;
        }

        return "redirect:/products/details/" + productId;
    }

    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(ID_ATTRIBUTE) UUID id, Model model) {
        model.addAttribute(PRODUCT_ATTRIBUTE, productService.findById(id, EditProductViewModel.class));
        return EDIT_VIEW;
    }

    @PreAuthorize(IS_MODERATOR)
    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute(PRODUCT_ATTRIBUTE) EditProductBindingModel product, Errors errors) {
        if (errors.hasErrors()) {
            return EDIT_VIEW;
        }

        productService.edit(product);
        return "redirect:/products/details/" + product.getId();
    }

    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID_ATTRIBUTE) UUID id, Model model) {
        model.addAttribute(PRODUCT_ATTRIBUTE, productService.findById(id, DeleteProductViewModel.class));
        return DELETE_VIEW;
    }

    @PreAuthorize(IS_MODERATOR)
    @DeleteMapping("/delete")
    public String deleteDelete(@Valid @ModelAttribute DeleteProductBindingModel model, Errors errors) {
        if (errors.hasErrors()) {
            return DELETE_VIEW;
        }

        productService.delete(model);
        return "redirect:/products/all";
    }

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }
}
