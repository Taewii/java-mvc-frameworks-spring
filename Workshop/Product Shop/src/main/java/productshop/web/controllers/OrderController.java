package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import productshop.domain.models.binding.order.OrderProductBindingModel;
import productshop.domain.models.view.order.OrderProductViewModel;
import productshop.services.OrderService;
import productshop.services.ProductService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final String ORDER_VIEWS_PATH = "order";
    private static final String ALL_VIEW = ORDER_VIEWS_PATH + "/" + "all";
    private static final String DETAILS_VIEW = ORDER_VIEWS_PATH + "/" + "details";
    private static final String FINALIZE_VIEW = ORDER_VIEWS_PATH + "/" + "finalize";
    private static final String MINE_VIEW = ORDER_VIEWS_PATH + "/" + "mine";

    private static final String ID_ATTRIBUTE = "id";
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String ORDERS_ATTRIBUTE = "orders";

    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService,
                           ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product/{id}")
    public String orderDetails(@PathVariable(ID_ATTRIBUTE) UUID id, Principal principal, Model model) {
        OrderProductViewModel product = productService.findById(id, OrderProductViewModel.class);
        product.setCustomer(principal.getName());
        model.addAttribute(PRODUCT_ATTRIBUTE, product);
        return FINALIZE_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/order")
    public String addToCart(@Valid @ModelAttribute(PRODUCT_ATTRIBUTE) OrderProductBindingModel order, Errors errors) {
        if (errors.hasErrors()) {
            return FINALIZE_VIEW;
        }

        orderService.addToCart(order);
        return "redirect:/home";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details/{id}")
    public String details(@PathVariable(ID_ATTRIBUTE) UUID id, Model model) {
        model.addAttribute(ORDER_ATTRIBUTE, orderService.findById(id));
        return DETAILS_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute(ORDERS_ATTRIBUTE, orderService.findAllOrdersWithUsers());
        return ALL_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mine")
    public String mine(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute(ORDERS_ATTRIBUTE, orderService.findAllFinalizedByUsername(username));
        return MINE_VIEW;
    }
}
