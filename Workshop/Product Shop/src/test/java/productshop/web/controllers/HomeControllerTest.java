package productshop.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.models.view.product.ListProductsViewModel;
import productshop.repositories.CategoryRepository;
import productshop.repositories.ProductRepository;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper mapper;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if (setUpIsDone) {
            return;
        }

        productRepository.deleteAll();
        categoryRepository.deleteAll();

        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("category " + i);
            Category categoryEntity = categoryRepository.saveAndFlush(category);

            Product product = new Product();
            product.setName("name " + i);
            product.setPrice(new BigDecimal(i));
            product.setImageUrl("imageUrl " + i);
            product.getCategories().add(categoryEntity);
            productRepository.saveAndFlush(product);
        }

        setUpIsDone = true;
    }

    @Test
    @WithAnonymousUser
    public void index_get_withAnonymousUser_returnsCorrectStatusCodeAndView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    public void index_get_withAuthenticatedUser_redirectsToCorrectView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/home?{title=\\w+}"));
    }

    @Test
    @WithMockUser
    public void home_get_withAuthenticatedUser_returnsCorrectViewAndModelAttribute() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", Matchers.hasSize(equalTo(5))));
    }

    @Test
    @WithAnonymousUser
    public void home_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithAnonymousUser
    public void getProductsFromCategory_get_withAnonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/api/products/0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void getProductsFromCategory_get_withAuthenticatedUserAndSpecificCategoryId_returnsCorrectData() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/products/2"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ListProductsViewModel[] listProductsViewModels = mapper.readValue(contentAsString, ListProductsViewModel[].class);

        assertEquals(1, listProductsViewModels.length);
        assertEquals(ListProductsViewModel.class, listProductsViewModels[0].getClass());
        assertEquals("name 1", listProductsViewModels[0].getName());
        assertEquals("1.00", listProductsViewModels[0].getPrice().toString());
        assertEquals("imageUrl 1", listProductsViewModels[0].getImageUrl());
    }

    @Test
    @WithMockUser
    public void getProductsFromCategory_get_withAuthenticatedUserAnd0AsId_returnsAllData() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/products/0"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ListProductsViewModel[] listProductsViewModels = mapper.readValue(contentAsString, ListProductsViewModel[].class);

        assertEquals(5, listProductsViewModels.length);

        for (int i = 0; i < listProductsViewModels.length; i++) {
            assertEquals(ListProductsViewModel.class, listProductsViewModels[i].getClass());
            assertEquals("name " + i, listProductsViewModels[i].getName());
            assertEquals(i + ".00", listProductsViewModels[i].getPrice().toString());
            assertEquals("imageUrl " + i, listProductsViewModels[i].getImageUrl());
        }
    }
}