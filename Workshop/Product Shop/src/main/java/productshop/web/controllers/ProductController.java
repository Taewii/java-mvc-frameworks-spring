package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import productshop.domain.models.binding.AddProductBindingModel;
import productshop.domain.models.binding.DeleteProductBindingModel;
import productshop.domain.models.binding.EditProductBindingModel;
import productshop.domain.models.view.DeleteProductViewModel;
import productshop.domain.models.view.DetailsProductViewModel;
import productshop.domain.models.view.EditProductViewModel;
import productshop.domain.models.view.ListCategoriesViewModel;
import productshop.services.CategoryService;
import productshop.services.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("product", productService.findById(id, DetailsProductViewModel.class));
        return "product/details";
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

        String productId = productService.add(product);
        if (productId == null) {
            errors.rejectValue("image", "400",
                    "File is either too large or not in the supported formats (jpg, jpeg, png, svg)");
            return "product/add";
        }

        return "redirect:/products/details/" + productId;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("product", productService.findById(id, EditProductViewModel.class));
        return "product/edit";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("product") EditProductBindingModel product, Errors errors) {
        if (errors.hasErrors()) {
            return "product/edit";
        }

        productService.edit(product);
        return "redirect:/products/details/" + product.getId();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("product", productService.findById(id, DeleteProductViewModel.class));
        return "product/delete";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/delete")
    public String deleteDelete(@Valid @ModelAttribute DeleteProductBindingModel model, Errors errors) {
        if (errors.hasErrors()) {
            return "product/delete";
        }

        productService.delete(model);
        return "redirect:/";
    }
}
