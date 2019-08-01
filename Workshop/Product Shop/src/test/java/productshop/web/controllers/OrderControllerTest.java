package productshop.web.controllers;

import org.hamcrest.Matchers;
import org.junit.Assert;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void orderDetails_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/orders/product/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void orderDetails_get_withAuthenticatedUserValidId_returnsCorrectViewStatusAndAttribute() throws Exception {
        Product product = new Product();
        product.setName("name");
        product.setPrice(BigDecimal.TEN);
        product.setImageUrl("imageUrl");
        Product productEntity = productRepository.saveAndFlush(product);

        mockMvc.perform(get("/orders/product/" + productEntity.getId().toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/finalize"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser
    public void orderDetails_get_withAuthenticatedUserInvalidId_returns4xxStatus() throws Exception {
        mockMvc.perform(get("/orders/product/INVALID"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    public void details_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/orders/details/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void details_get_withAuthenticatedUserValidId_returnsCorrectViewStatusAndAttribute() throws Exception {
        Order order = createOrder(1);

        mockMvc.perform(get("/orders/details/" + order.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/details"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    @WithMockUser
    public void details_get_withAuthenticatedUserInValidId_returns4xxStatus() throws Exception {
        mockMvc.perform(get("/orders/details/INVALID"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    public void all_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/orders/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void all_get_withAuthenticatedUser_returnsCorrectStatusCodeViewAndAttribute() throws Exception {
        createOrder(1);
        createOrder(2);
        createOrder(3);

        mockMvc.perform(get("/orders/all"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/all"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", Matchers.hasSize(equalTo(3))));
    }

    @Test
    @WithAnonymousUser
    public void mine_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/orders/mine"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "username 1")
    public void mine_get_withAuthenticatedUser_returnsCorrectStatusCodeViewAndAttribute() throws Exception {
        createOrder(1);
        createOrder(2);
        createOrder(3);

        mockMvc.perform(get("/orders/mine"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/mine"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", Matchers.hasSize(equalTo(1))));
    }

    @Test
    @WithAnonymousUser
    public void orderPost_post_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(post("/orders/order")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void orderPost_post_withAuthenticatedUserValidInput_returnsCorrectStatusCodeViewAndAttribute() throws Exception {
        createOrder(1);

        Product product = new Product();
        product.setName("name");
        product.setPrice(BigDecimal.TEN);
        product.setImageUrl("imageUrl");
        Product productEntity = productRepository.saveAndFlush(product);

        mockMvc.perform(post("/orders/order")
                .with(csrf())
                .param("productId", productEntity.getId().toString())
                .param("customer", "username 1")
                .param("name", "name")
                .param("price", String.valueOf(BigDecimal.TEN))
                .param("imageUrl", "imageUrl")
                .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        Assert.assertEquals(2, orderRepository.count());
    }

    @Test
    @WithMockUser
    public void orderPost_post_withAuthenticatedUserInvalidInput_returnsFinalizeView() throws Exception {
        mockMvc.perform(post("/orders/order")
                .with(csrf())
                .param("productId", "invalid")
                .param("customer", "invalid")
                .param("name", "")
                .param("price", String.valueOf(BigDecimal.ZERO))
                .param("imageUrl", "imageUrl")
                .param("quantity", "-1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/finalize"));
    }

    private Order createOrder(int postfix) {
        Product product = new Product();
        product.setName("name " + postfix);
        product.setPrice(new BigDecimal(postfix));
        product.setImageUrl("imageUrl " + postfix);
        Product productEntity = productRepository.saveAndFlush(product);

        Role role = new Role(Authority.ROOT);
        Role roleEntity = roleRepository.saveAndFlush(role);

        User user = new User();
        user.setUsername("username " + postfix);
        user.setEmail("email@emai.email");
        user.setPassword("password");
        user.setRoles(new HashSet<>() {{
            add(roleEntity);
        }});
        User userEntity = userRepository.saveAndFlush(user);

        Order order = new Order();
        order.setProduct(productEntity);
        order.setCustomer(userEntity);
        order.setQuantity(postfix);
        order.setTotalPrice(new BigDecimal(postfix));
        order.setOrderDate(LocalDateTime.of(2000, 1, 2, 1, 1, 1));
        return orderRepository.saveAndFlush(order);
    }
}