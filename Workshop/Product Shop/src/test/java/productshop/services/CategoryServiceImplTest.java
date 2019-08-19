package productshop.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.models.binding.category.AddCategoryBindingModel;
import productshop.domain.models.binding.category.EditCategoryBindingModel;
import productshop.domain.models.view.category.EditCategoryViewModel;
import productshop.domain.models.view.category.ListCategoriesViewModel;
import productshop.domain.models.view.product.ListProductsViewModel;
import productshop.repositories.CategoryRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findAll_with2categories_returnsCorrect2Categories() {
        Category category1 = new Category();
        category1.setName("category 1");

        Category category2 = new Category();
        category2.setName("category 2");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<ListCategoriesViewModel> all = categoryService.findAll();

        assertEquals(2, all.size());
        assertEquals("category 1", all.get(0).getName());
        assertEquals("category 2", all.get(1).getName());
    }

    @Test
    public void findAll_withNoCategories_returnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        List<ListCategoriesViewModel> all = categoryService.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    public void findById_withValidId_returnsCorrectlyMappedClassAndValues() {
        Category category = new Category();
        category.setId(1L);
        category.setName("category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        EditCategoryViewModel result = categoryService.findById("1", EditCategoryViewModel.class);

        assertEquals(EditCategoryViewModel.class, result.getClass());
        assertEquals(Long.valueOf(1), result.getId());
        assertEquals("category", result.getName());
    }

    @Test(expected = NoSuchElementException.class)
    public void findById_withInvalidId_throwsNoSuchElementException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        categoryService.findById("1", EditCategoryViewModel.class);
    }

    @Test
    public void remove_withValidCategoryName_removesCategory() {
        String categoryName = "category";
        Category category = new Category();
        category.setName(categoryName);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        categoryService.remove(categoryName);

        verify(categoryRepository).delete(category);
    }

    @Test(expected = NoSuchElementException.class)
    public void remove_withInValidCategoryName_throwsNoSuchElementException() {
        String categoryName = "category";
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());
        categoryService.remove(categoryName);
    }

    @Test
    public void getProductsByCategoryId_withValid2ProductsInput_returnsCorrectData() {
        Category category = new Category();
        category.setId(1L);
        category.setProducts(createProducts(2));
        when(categoryRepository.findByIdEager(1L)).thenReturn(Optional.of(category));

        List<ListProductsViewModel> result = categoryService.getProductsByCategoryId(1L);

        List<String> products = List.of("Product 1", "Product 2");
        List<String> imageUrls = List.of("ImageUrl 1", "ImageUrl 2");
        List<BigDecimal> prices = List.of(BigDecimal.ONE, new BigDecimal(2));

        assertEquals(2, result.size());
        for (ListProductsViewModel listProductsViewModel : result) {
            assertEquals(ListProductsViewModel.class, listProductsViewModel.getClass());
            assertTrue(products.contains(listProductsViewModel.getName()));
            assertTrue(imageUrls.contains(listProductsViewModel.getImageUrl()));
            assertTrue(prices.contains(listProductsViewModel.getPrice()));
        }
    }

    @Test
    public void getProductsByCategoryId_withNoProducts_returnsEmptySet() {
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findByIdEager(1L)).thenReturn(Optional.of(category));

        List<ListProductsViewModel> result = categoryService.getProductsByCategoryId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getProductsByCategoryId_withInvalidInput_throwsNoSuchElementException() {
        when(categoryRepository.findByIdEager(1L)).thenReturn(Optional.empty());
        List<ListProductsViewModel> result = categoryService.getProductsByCategoryId(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void add_withValidModel_successfullyCreatesEntityAndReturnsTrue() {
        AddCategoryBindingModel model = mock(AddCategoryBindingModel.class);
        when(categoryRepository.findByName(any())).thenReturn(Optional.empty());

        boolean result = categoryService.add(model);

        assertTrue(result);
        verify(categoryRepository).saveAndFlush(any(Category.class));
    }

    @Test
    public void add_withDuplicateModel_doesntCreateEntityAndReturnsFalse() {
        AddCategoryBindingModel model = mock(AddCategoryBindingModel.class);
        Category category = mock(Category.class);
        when(categoryRepository.findByName(any())).thenReturn(Optional.of(category));

        boolean result = categoryService.add(model);

        assertFalse(result);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    public void add_withNull_returnsFalse() {
        boolean result = categoryService.add(null);
        assertFalse(result);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    public void edit_withValidModel_successfullyEditsAndReturnsTrue() {
        EditCategoryBindingModel model = mock(EditCategoryBindingModel.class);
        when(model.getName()).thenReturn("Old name");

        Category category = mock(Category.class);
        doCallRealMethod().when(category).setName(any(String.class));
        when(category.getName()).thenCallRealMethod();

        when(categoryRepository.findByName(any())).thenReturn(Optional.empty());
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        boolean result = categoryService.edit(model);

        assertTrue(result);
        assertEquals("Old name", category.getName());
        verify(categoryRepository).saveAndFlush(category);
    }

    @Test
    public void edit_withDuplicateModel_doesntEditEntityAndReturnsFalse() {
        EditCategoryBindingModel model = mock(EditCategoryBindingModel.class);
        Category category = mock(Category.class);
        when(categoryRepository.findByName(any())).thenReturn(Optional.of(category));

        boolean result = categoryService.edit(model);

        assertFalse(result);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void edit_withInvalidCategoryId_throwsNoSuchElementException() {
        EditCategoryBindingModel model = mock(EditCategoryBindingModel.class);
        when(categoryRepository.findByName(any())).thenReturn(Optional.empty());
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        categoryService.edit(model);

        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    public void edit_withNull_returnsFalse() {
        boolean result = categoryService.edit(null);
        assertFalse(result);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    private Set<Product> createProducts(int count) {
        Set<Product> products = new HashSet<>();

        for (int i = 1; i <= count; i++) {
            Product product = new Product();
            product.setId(UUID.randomUUID());
            product.setName("Product " + i);
            product.setPrice(new BigDecimal(i));
            product.setImageUrl("ImageUrl " + i);
            products.add(product);
        }

        return products;
    }
}