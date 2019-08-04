package productshop.web.controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import productshop.domain.entities.Order;
import productshop.domain.entities.Product;
import productshop.domain.entities.Role;
import productshop.domain.entities.User;
import productshop.domain.enums.Authority;
import productshop.repositories.OrderRepository;
import productshop.repositories.ProductRepository;
import productshop.repositories.RoleRepository;
import productshop.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void apiOrders_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/cart/api/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void apiOrders_get_withAuthenticateUser_returnsCorrectData() throws Exception {
        User user = createUser("user");
        createOrder(1, user, false);
        createOrder(2, user, false);
        createOrder(3, user, false);
        createOrder(4, user, true);

        mockMvc.perform(get("/cart/api/orders"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithAnonymousUser
    public void details_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/cart/details"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void details_get_withAuthenticateUser_returnsViewStatusCodeAndAttributes() throws Exception {
        User user = createUser("user");
        createOrder(1, user, false);
        createOrder(2, user, false);
        createOrder(3, user, false);
        createOrder(4, user, true);

        mockMvc.perform(get("/cart/details"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("cart/details"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", Matchers.hasSize(equalTo(3))));
    }

    @Test
    @WithAnonymousUser
    public void remove_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/cart/remove/" + UUID.randomUUID().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void remove_get_withAuthenticateValidUser_removesOrderAndRedirectsCorrectly() throws Exception {
        User user = createUser("user");
        createOrder(1, user, false);
        Order order = createOrder(2, user, false);
        createOrder(3, user, false);
        createOrder(4, user, true);

        assertEquals(4, orderRepository.count());

        mockMvc.perform(get("/cart/remove/" + order.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/details"));

        assertEquals(3, orderRepository.count());
    }

    @Test(expected = Exception.class)
    @WithMockUser(username = "user")
    public void remove_get_withUserTryingToRemoveOrderThatHeHasNotMade_throwsIllegalArgumentException() throws Exception {
        createUser("user");
        User otherUser = createUser("otherUser");
        Order order = createOrder(2, otherUser, false);

        mockMvc.perform(get("/cart/remove/" + order.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/details"));
    }

    @Test
    @WithAnonymousUser
    public void checkout_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/cart/checkout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void checkout_get_withAuthenticatedUser_finalizesOrdersAndRedirects() throws Exception {
        User user = createUser("user");
        createOrder(1, user, false);
        createOrder(2, user, false);
        createOrder(3, user, false);
        createOrder(4, user, true);

        assertEquals(1, orderRepository.findAllFinalizedByUsernameEager("user").size());
        assertEquals(3, orderRepository.findAllNotFinalizedByUsernameEager("user").size());

        mockMvc.perform(get("/cart/checkout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        assertEquals(4, orderRepository.findAllFinalizedByUsernameEager("user").size());
        assertEquals(0, orderRepository.findAllNotFinalizedByUsernameEager("user").size());
    }

    private User createUser(String username) {
        Role role = new Role(Authority.ROOT);
        Role roleEntity = roleRepository.saveAndFlush(role);

        User user = new User();
        user.setUsername(username);
        user.setEmail("email@emai.email");
        user.setPassword("password");
        user.setRoles(new HashSet<>() {{
            add(roleEntity);
        }});

        return userRepository.saveAndFlush(user);
    }

    private Order createOrder(int postfix, User user, boolean isFinalized) {
        Product product = new Product();
        product.setName("name " + postfix);
        product.setPrice(new BigDecimal(postfix));
        product.setImageUrl("imageUrl " + postfix);
        Product productEntity = productRepository.saveAndFlush(product);

        Order order = new Order();
        order.setFinalized(isFinalized);
        order.setProduct(productEntity);
        order.setCustomer(user);
        order.setQuantity(postfix);
        order.setTotalPrice(new BigDecimal(postfix));
        order.setOrderDate(LocalDateTime.of(2000, 1, 2, 1, 1, 1));
        return orderRepository.saveAndFlush(order);
    }
}