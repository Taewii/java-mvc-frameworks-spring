package productshop.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import productshop.domain.entities.Category;
import productshop.domain.models.view.category.EditCategoryViewModel;
import productshop.domain.models.view.category.ListCategoriesViewModel;
import productshop.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}