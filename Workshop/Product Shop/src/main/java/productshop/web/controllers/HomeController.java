package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import productshop.domain.models.view.product.ListProductsViewModel;
import productshop.services.CategoryService;
import productshop.services.ProductService;

import java.util.List;

@Controller
public class HomeController {

    private static final String INDEX_VIEW = "index";
    private static final String HOME_VIEW = "home";

    private static final String ID_ATTRIBUTE = "id";
    private static final String CATEGORIES_ATTRIBUTE = "categories";

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
        return INDEX_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute(CATEGORIES_ATTRIBUTE, categoryService.findAll());
        return HOME_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/{id}")
    @ResponseBody
    public List<ListProductsViewModel> getProductsFromCategory(@PathVariable(ID_ATTRIBUTE) Long id) {
        if (id == 0) {
            return productService.findAll();
        }
        return categoryService.getProductsByCategoryId(id);
    }
}
