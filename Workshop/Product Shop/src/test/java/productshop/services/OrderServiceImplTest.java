package productshop.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void findById_withCorrectInput_returnsCorrectlyMappedData() {
        Order order = createOrdersWithProductAndUser(1).get(0);

        when(orderRepository.findByIdEager(any(UUID.class))).thenReturn(Optional.of(order));

        OrderDetailsViewModel result = orderService.findById(UUID.randomUUID());

        assertEquals(OrderDetailsViewModel.class, result.getClass());
        assertEquals("username 0", result.getCustomerUsername());
        assertEquals("name 0", result.getProductName());
        assertEquals("description 0", result.getProductDescription());
        assertEquals("imageUrl 0", result.getProductImageUrl());
        assertEquals(BigDecimal.ZERO, result.getTotalPrice());
        assertEquals(Integer.valueOf(0), result.getQuantity());
    }

    @Test(expected = NoSuchElementException.class)
    public void findById_withInvalidId_throwsNoSuchElementException() {
        when(orderRepository.findByIdEager(any())).thenReturn(Optional.empty());
        orderService.findById(UUID.randomUUID());
    }

    @Test
    public void findAllOrdersWithUsers_with3Orders_returnsCorrectlyMapped3Orders() {
        List<Order> orders = createOrdersWithProductAndUser(3);
        when(orderRepository.findAllEager()).thenReturn(orders);

        List<ListOrdersViewModel> result = orderService.findAllOrdersWithUsers();

        assertEquals(3, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(ListOrdersViewModel.class, result.get(i).getClass());
            assertEquals("username " + i, result.get(i).getCustomerUsername());
            assertEquals("imageUrl " + i, result.get(i).getProductImageUrl());
            assertEquals(new BigDecimal(i), result.get(i).getTotalPrice());
        }
    }

    @Test
    public void findAllOrdersWithUsers_withNoOrders_returnsEmptyList() {
        List<ListOrdersViewModel> result = orderService.findAllOrdersWithUsers();
        assertTrue(result.isEmpty());
    }

    @Test // kinda needs in-memory db to test the query
    public void findAllByUsername_with3Orders_returnsCorrectlyMappedOrder() {
        List<Order> orders = createOrdersWithProductAndUser(3);
        when(orderRepository.findAllByUsernameEager("username 1")).thenReturn(List.of(orders.get(1)));

        List<ListOrdersViewModel> result = orderService.findAllByUsername("username 1");
        ListOrdersViewModel resultModel = result.get(0);

        assertEquals(1, result.size());
        assertEquals(ListOrdersViewModel.class, resultModel.getClass());
        assertEquals("username 1", resultModel.getCustomerUsername());
        assertEquals("imageUrl 1", resultModel.getProductImageUrl());
        assertEquals(BigDecimal.ONE, resultModel.getTotalPrice());
    }

    @Test
    public void findAllByUsername_withNoOrders_returnsEmptyList() {
        List<ListOrdersViewModel> result = orderService.findAllByUsername("username 1");
        assertTrue(result.isEmpty());
    }

    @Test
    public void order_withValidData_successfullyCreatesOrder() {
        User user = mock(User.class);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        Product product = mock(Product.class);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        OrderProductBindingModel orderModel = mock(OrderProductBindingModel.class);
        when(orderModel.getPrice()).thenReturn(BigDecimal.TEN);
        when(orderModel.getQuantity()).thenReturn(2);

        // not sure how to test the setters
        orderService.addToCart(orderModel);
        verify(orderRepository).saveAndFlush(any(Order.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void order_withNullUser_throwsNoSuchElementException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        Product product = mock(Product.class);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        OrderProductBindingModel orderModel = mock(OrderProductBindingModel.class);
        when(orderModel.getPrice()).thenReturn(BigDecimal.TEN);
        when(orderModel.getQuantity()).thenReturn(2);

        orderService.addToCart(orderModel);
    }

    @Test(expected = NoSuchElementException.class)
    public void order_withNullProduct_throwsNoSuchElementException() {
        User user = mock(User.class);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(productRepository.findById(any(UUID.class))).thenReturn(null);

        OrderProductBindingModel orderModel = mock(OrderProductBindingModel.class);
        when(orderModel.getPrice()).thenReturn(BigDecimal.TEN);
        when(orderModel.getQuantity()).thenReturn(2);

        orderService.addToCart(orderModel);
    }

    @Test(expected = NoSuchElementException.class)
    public void order_withNullProductAndUser_throwsNoSuchElementException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);
        when(productRepository.findById(any(UUID.class))).thenReturn(null);

        OrderProductBindingModel orderModel = mock(OrderProductBindingModel.class);
        when(orderModel.getPrice()).thenReturn(BigDecimal.TEN);
        when(orderModel.getQuantity()).thenReturn(2);

        orderService.addToCart(orderModel);
    }

    @Test(expected = NullPointerException.class)
    public void order_withNullInput_throwsNullPointerException() {
        orderService.addToCart(null);
    }

    private List<Order> createOrdersWithProductAndUser(int count) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUsername("username " + i);

            Product product = new Product();
            product.setImageUrl("imageUrl " + i);
            product.setName("name " + i);
            product.setDescription("description " + i);
            product.setPrice(new BigDecimal(i));

            Order order = new Order();
            order.setCustomer(user);
            order.setProduct(product);
            order.setQuantity(i);
            order.setTotalPrice(new BigDecimal(i));

            orders.add(order);
        }

        return orders;
    }
}