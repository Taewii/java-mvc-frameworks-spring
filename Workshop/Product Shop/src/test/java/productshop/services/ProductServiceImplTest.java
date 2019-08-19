package productshop.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.models.binding.product.AddProductBindingModel;
import productshop.domain.models.binding.product.DeleteProductBindingModel;
import productshop.domain.models.binding.product.EditProductBindingModel;
import productshop.domain.models.view.order.OrderProductViewModel;
import productshop.domain.models.view.product.EditProductViewModel;
import productshop.domain.models.view.product.ListProductsViewModel;
import productshop.repositories.CategoryRepository;
import productshop.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private GoogleDriveService googleDriveService;

    @Captor
    private ArgumentCaptor captor;

    @Test
    public void add_withValidInput_addsProductAndReturnsItsId() {
        when(googleDriveService.uploadFile(any())).thenReturn("imageId");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category() {{
            setId(1L);
        }}));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(new Category() {{
            setId(2L);
        }}));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(new Category() {{
            setId(3L);
        }}));

        Product product = new Product();
        UUID id = UUID.randomUUID();
        product.setId(id);
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);

        AddProductBindingModel model = new AddProductBindingModel();
        model.setName("name");
        model.setDescription("description");
        model.setPrice(BigDecimal.TEN);
        model.setCategories(new ArrayList<>() {{
            add(1L);
            add(2L);
            add(3L);
        }});

        String result = productService.add(model);

        assertEquals(id.toString(), result);
        verify(productRepository).saveAndFlush(any(Product.class));
    }

    @Test
    public void add_withValidInvalidFile_returnsNullAmdStopsMethodExecution() {
        when(googleDriveService.uploadFile(any())).thenThrow(IllegalArgumentException.class);
        AddProductBindingModel model = new AddProductBindingModel();

        String result = productService.add(model);

        assertNull(result);
        verify(productRepository, never()).saveAndFlush(any(Product.class));
    }

    @Test
    public void findByIdEager_withValidInput_returnsCorrectlyMappedProduct() {
        Product product = new Product();
        product.setName("name");
        product.setDescription("description");
        product.setImageUrl("imageUrl");
        product.setPrice(BigDecimal.TEN);

        Set<Category> categories = new HashSet<>();
        for (long i = 1; i <= 3; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("category " + i);
            categories.add(category);
        }

        product.setCategories(categories);

        when(productRepository.findByIdEager(any(UUID.class))).thenReturn(Optional.of(product));

        EditProductViewModel result = productService.findByIdEager(UUID.randomUUID(), EditProductViewModel.class);

        assertEquals(EditProductViewModel.class, result.getClass());
        assertEquals("name", result.getName());
        assertEquals("description", result.getDescription());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals(3, result.getCategories().size());
        assertTrue(result.getCategories().contains(1L));
        assertTrue(result.getCategories().contains(2L));
        assertTrue(result.getCategories().contains(3L));
    }

    @Test(expected = NoSuchElementException.class)
    public void findByIdEager_withNonExistingProduct_throwsNoSuchElementException() {
        productService.findByIdEager(UUID.randomUUID(), OrderProductViewModel.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByIdEager_withNullTargetClass_throwsIllegalArgumentException() {
        Product product = new Product();
        when(productRepository.findByIdEager(any(UUID.class))).thenReturn(Optional.of(product));

        productService.findByIdEager(UUID.randomUUID(), null);
    }

    @Test
    public void findById_withValidInput_returnsCorrectlyMappedProduct() {
        Product product = new Product();
        product.setName("name");
        product.setDescription("description");
        product.setImageUrl("imageUrl");
        product.setPrice(BigDecimal.TEN);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        OrderProductViewModel result = productService.findById(UUID.randomUUID(), OrderProductViewModel.class);

        assertEquals(OrderProductViewModel.class, result.getClass());
        assertEquals("name", result.getName());
        assertEquals("description", result.getDescription());
        assertEquals("imageUrl", result.getImageUrl());
        assertEquals(BigDecimal.TEN, result.getPrice());
    }

    @Test(expected = NoSuchElementException.class)
    public void findById_withNonExistingProduct_throwsNoSuchElementException() {
        productService.findById(UUID.randomUUID(), OrderProductViewModel.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findById_withNullTargetClass_throwsIllegalArgumentException() {
        Product product = new Product();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        productService.findById(UUID.randomUUID(), null);
    }

    @Test
    public void findAll_withValid3Products_returnsCorrectlyMapped3Products() {
        List<Product> products = createProducts(3);
        when(productRepository.findAll()).thenReturn(products);

        List<ListProductsViewModel> result = productService.findAll();

        assertEquals(3, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(ListProductsViewModel.class, result.get(i).getClass());
            assertEquals("name " + i, products.get(i).getName());
            assertEquals("imageUrl " + i, products.get(i).getImageUrl());
            assertEquals(new BigDecimal(i), products.get(i).getPrice());
        }
    }

    @Test
    public void findAll_withNoProducts_returnsEmptyList() {
        List<ListProductsViewModel> result = productService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void edit_withValidInput_editsSuccessfully() {
        when(googleDriveService.uploadFile(any())).thenReturn("imageId");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category() {{
            setId(1L);
        }}));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(new Category() {{
            setId(2L);
        }}));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(new Category() {{
            setId(3L);
        }}));

        Product product = mock(Product.class);
        when(productRepository.findByIdEager(any(UUID.class))).thenReturn(Optional.of(product));

        EditProductBindingModel model = new EditProductBindingModel();
        model.setId(UUID.randomUUID());
        model.setName("name");
        model.setDescription("description");
        model.setPrice(BigDecimal.TEN);
        model.setCategories(new ArrayList<>() {{
            add(1L);
            add(2L);
            add(3L);
        }});

        productService.edit(model);
        verify(product).setName(String.valueOf(captor.capture()));
        verify(product).setDescription(String.valueOf(captor.capture()));
        verify(product).setPrice((BigDecimal) captor.capture());

        List allValues = captor.getAllValues();

        assertTrue(allValues.contains("name"));
        assertTrue(allValues.contains("description"));
        assertTrue(allValues.contains(BigDecimal.TEN));
        verify(productRepository).saveAndFlush(any(Product.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void edit_withNonExistingProductId_throwsNoSuchElementException() {
        EditProductBindingModel model = mock(EditProductBindingModel.class);
        when(model.getId()).thenReturn(UUID.randomUUID());
        productService.edit(model);
    }

    @Test(expected = NoSuchElementException.class)
    public void edit_withNonExistingCategoryId_throwsNoSuchElementException() {
        Product product = mock(Product.class);
        when(productRepository.findByIdEager(any(UUID.class))).thenReturn(Optional.of(product));

        EditProductBindingModel model = new EditProductBindingModel();
        model.setId(UUID.randomUUID());
        model.setName("name");
        model.setDescription("description");
        model.setPrice(BigDecimal.TEN);
        model.setCategories(new ArrayList<>() {{
            add(1L);
            add(2L);
            add(3L);
        }});

        productService.edit(model);
    }

    @Test
    public void delete_withValidInput_deletesImageAndProduct() {
        DeleteProductBindingModel model = mock(DeleteProductBindingModel.class);
        when(model.getId()).thenReturn(UUID.randomUUID());

        Product product = mock(Product.class);
        when(product.getImageUrl()).thenReturn("imageUrl");

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        productService.delete(model);

        verify(googleDriveService).delete(any(String.class));
        verify(productRepository).delete(any(Product.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void delete_withInvalidProductId_throwsNoSuchElementException() {
        DeleteProductBindingModel model = mock(DeleteProductBindingModel.class);
        productService.delete(model);
    }

    private List<Product> createProducts(int count) {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName("name " + i);
            product.setImageUrl("imageUrl " + i);
            product.setDescription("description " + i);
            product.setPrice(new BigDecimal(i));
            products.add(product);
        }

        return products;
    }
}