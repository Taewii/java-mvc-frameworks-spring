package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import productshop.domain.annotations.PageTitle;
import productshop.domain.models.view.order.CartDetailsOrderViewModel;
import productshop.domain.models.view.order.CartViewOrderModel;
import productshop.services.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final String CART_VIEWS_PATH = "cart";
    private static final String DETAILS_VIEW = CART_VIEWS_PATH + "/" + "details";

    private final OrderService orderService;

    @Autowired
    public CartController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/orders")
    @ResponseBody
    public List<CartViewOrderModel> cartItems(Principal principal) {
        return orderService.findAllNotFinalizedByUsername(principal.getName(), CartViewOrderModel.class);
    }

    @PageTitle(text = "Cart Details")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details")
    public String details(Principal principal, Model model) {
        List<CartDetailsOrderViewModel> orders = orderService
                .findAllNotFinalizedByUsername(principal.getName(), CartDetailsOrderViewModel.class);
        model.addAttribute("orders", orders);
        return DETAILS_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable UUID id, Principal principal) {
        orderService.remove(id, principal.getName());
        return "redirect:/cart/details";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkout")
    public String checkout(Principal principal) {
        orderService.checkout(principal.getName());
        return "redirect:/home";
    }
}
