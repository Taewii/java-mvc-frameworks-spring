package productshop.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import productshop.config.Constants;
import productshop.domain.entities.Order;
import productshop.domain.entities.Product;
import productshop.domain.entities.User;
import productshop.domain.models.binding.order.OrderProductBindingModel;
import productshop.domain.models.view.order.ListOrdersViewModel;
import productshop.domain.models.view.order.OrderDetailsViewModel;
import productshop.repositories.OrderRepository;
import productshop.repositories.ProductRepository;
import productshop.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository,
                            ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void order(OrderProductBindingModel model) {
        User user = userRepository.findByUsername(model.getCustomer()).orElseThrow();
        Product product = productRepository.findById(model.getProductId()).orElseThrow();

        Order order = mapper.map(model, Order.class);
        order.setCustomer(user);
        order.setProduct(product);
        order.setTotalPrice(model.getPrice().multiply(new BigDecimal(Math.floor(model.getQuantity()))));
        order.setOrderDate(LocalDateTime.now());
        order.setId(null); // it maps the id to be the model.productId

        orderRepository.saveAndFlush(order);
    }

    @Override
    public OrderDetailsViewModel findById(UUID id) {
        return mapper.map(orderRepository.findByIdEager(id).orElseThrow(), OrderDetailsViewModel.class);
    }

    @Override
    public List<ListOrdersViewModel> findAllOrdersWithUsers() {
        return orderRepository
                .findAllEager()
                .stream()
                .map(o -> mapper.map(o, ListOrdersViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ListOrdersViewModel> findAllByUsername(String username) {
        return orderRepository
                .findAllByUsernameEager(username)
                .stream()
                .map(o -> mapper.map(o, ListOrdersViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
