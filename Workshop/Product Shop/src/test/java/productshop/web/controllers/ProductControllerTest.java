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
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.repositories.CategoryRepository;
import productshop.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void clear() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    private Product createProduct(String name) {
        Category category = new Category();
        category.setName(name);
        Category categoryEntity = categoryRepository.saveAndFlush(category);

        Product product = new Product();
        product.setName(name);
        product.setImageUrl("imageUrl");
        product.setPrice(BigDecimal.TEN);
        product.getCategories().add(categoryEntity);

        return productRepository.saveAndFlush(product);
    }

    @Test
    @WithAnonymousUser
    public void all_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/products/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void all_get_withUserBelowModeratorRole_isForbidden() throws Exception {
        mockMvc.perform(get("/products/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void add_get_withModeratorUser_returnsCorrectStatusViewAndAttribute() throws Exception {
        mockMvc.perform(get("/products/add/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/add"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void all_get_withUserWithModeratorRole_returnsAllProducts() throws Exception {
        createProduct("p1");
        createProduct("p2");
        createProduct("p3");

        mockMvc.perform(get("/products/all"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/all"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Matchers.hasSize(equalTo(3))));
    }

    @Test
    @WithAnonymousUser
    public void details_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/products/details/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void details_get_withAuthenticatedUser_returnsCorrectStatusViewAndAttributes() throws Exception {
        Product product = createProduct("name");

        mockMvc.perform(get("/products/details/" + product.getId().toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/details"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("productId"));
    }

    @Test
    @WithAnonymousUser
    public void add_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/products/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void add_get_withUserBelowModeratorRole_isForbidden() throws Exception {
        mockMvc.perform(get("/products/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void addPost_post_withModeratorUserValidData_addsProductAndRedirectsCorrectly() throws Exception {

        Category category = new Category();
        category.setName("category");
        Category categoryEntity = categoryRepository.save(category);

        mockMvc.perform(post("/products/add")
                .with(csrf())
                .param("name", "name")
                .param("price", "10.00")
                .param("categories", String.valueOf(categoryEntity.getId()))
                .param("image", "noclue")) // TODO: 31.7.2019 г. no idea how to do the image
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/products/details/*"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void addPost_post_withModeratorUserInValidData_returnsCorrectView() throws Exception {
        mockMvc.perform(post("/products/add")
                .with(csrf())
                .param("name", "")
                .param("price", "-10.00")
                .param("categories", "")
                .param("image", "noclue")) // TODO: 31.7.2019 г. same problem with the image
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/add"));
    }

    @Test
    @WithAnonymousUser
    public void edit_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void edit_get_withUserBelowModeratorRole_isForbidden() throws Exception {
        mockMvc.perform(get("/products/edit/" + UUID.randomUUID().toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void edit_get_withModeratorUser_returnsCorrectStatusViewAndAttribute() throws Exception {
        Product product = createProduct("product");

        mockMvc.perform(get("/products/edit/" + product.getId().toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/edit"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void editPost_post_withModeratorUserValidData_editsProductAndRedirectsCorrectly() throws Exception {
        Category category = new Category();
        category.setName("category");
        Category categoryEntity = categoryRepository.save(category);

        Product product = createProduct("product");

        mockMvc.perform(post("/products/edit")
                .with(csrf())
                .param("id", product.getId().toString())
                .param("name", "new name")
                .param("price", "100.00")
                .param("categories", String.valueOf(categoryEntity.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/products/details/*"));

        Product result = productRepository.findById(product.getId()).orElseThrow();

        assertEquals("new name", result.getName());
        assertEquals("100.00", result.getPrice().toString());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void editPost_post_withModeratorUserInValidData_returnsCorrectView() throws Exception {
        mockMvc.perform(post("/products/edit")
                .with(csrf())
                .param("id", UUID.randomUUID().toString())
                .param("name", "invalid")
                .param("price", "-20")
                .param("categories", ""))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/edit"));
    }

    @Test
    @WithAnonymousUser
    public void delete_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/products/delete/" + UUID.randomUUID().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void delete_get_withUserBelowModeratorRole_isForbidden() throws Exception {
        mockMvc.perform(get("/products/delete/" + UUID.randomUUID().toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void delete_delete_withModeratorUser_returnsCorrectStatusViewAndAttribute() throws Exception {
        Product product = createProduct("product");

        mockMvc.perform(get("/products/delete/" + product.getId().toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/delete"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void delete_delete_withModeratorUserValidId_deletesProductAndRedirectsCorrectly() throws Exception {
        Product product = createProduct("product");

        mockMvc.perform(delete("/products/delete")
                .with(csrf())
                .param("id", product.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/all"));

        assertFalse(productRepository.findById(product.getId()).isPresent());
    }

    @Test(expected = Exception.class)
    @WithMockUser(roles = "MODERATOR")
    public void delete_delete_withModeratorUserNullId_throwsNoSuchElementException() throws Exception {
        mockMvc.perform(delete("/products/delete")
                .with(csrf())
                .param("id", UUID.randomUUID().toString()));

    }
}