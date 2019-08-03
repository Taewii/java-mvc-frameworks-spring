package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import productshop.domain.models.view.order.CartViewOrderModel;
import productshop.services.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final OrderService orderService;

    @Autowired
    public CartController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/orders")
    @ResponseBody
    public List<CartViewOrderModel> cartItems(Principal principal) {
        return orderService.findAllNotFinalizedByUsername(principal.getName());
    }
}
