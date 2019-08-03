package productshop.services;

import productshop.domain.models.binding.order.OrderProductBindingModel;
import productshop.domain.models.view.order.CartViewOrderModel;
import productshop.domain.models.view.order.ListOrdersViewModel;
import productshop.domain.models.view.order.OrderDetailsViewModel;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    void addToCart(OrderProductBindingModel model);

    OrderDetailsViewModel findById(UUID id);

    List<ListOrdersViewModel> findAllOrdersWithUsers();

    List<ListOrdersViewModel> findAllFinalizedByUsername(String username);

    List<CartViewOrderModel> findAllNotFinalizedByUsername(String username);
}
