package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import productshop.domain.models.binding.AddProductBindingModel;
import productshop.domain.models.view.ListCategoriesViewModel;
import productshop.services.CategoryService;
import productshop.services.ProductService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<ListCategoriesViewModel> categories() {
        return categoryService.findAll();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("product", new AddProductBindingModel());
        return "product/add";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("product") AddProductBindingModel product, Errors errors) {
        boolean hasImage = product.getImage().getSize() > 0;
        if (errors.hasErrors() || !hasImage) {
            if (!hasImage) {
                errors.rejectValue("image", "400", "Please select an image.");
            }
            return "product/add";
        }

        boolean isSuccessful = productService.add(product);
        if (!isSuccessful) {
            errors.rejectValue("image", "400",
                    "File is either too large or not in the supported formats (jpg, jpeg, png, svg)");
            return "product/add";
        }
        https:
//drive.google.com/uc?id=1nBaxmBudJsdupH1zcm9JHV-Mbqc9LVBO to retrieve in html
        return "redirect:/";
    }
}
