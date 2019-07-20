package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import productshop.domain.models.view.ListProductsViewModel;
import productshop.services.CategoryService;
import productshop.services.ProductService;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public HomeController(CategoryService categoryService,
                          ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication != null) { // TODO: 20.7.2019 г. ghetto check if user is 'remember-me' fix me
            return "redirect:/home";
        }
        return "index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "home";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/{id}")
    @ResponseBody
    public List<ListProductsViewModel> getProductsFromCategory(@PathVariable("id") Long id) {
        if (id == 0) {
            return productService.findAll();
        }
        return categoryService.getProductsByCategoryId(id);
    }
}
