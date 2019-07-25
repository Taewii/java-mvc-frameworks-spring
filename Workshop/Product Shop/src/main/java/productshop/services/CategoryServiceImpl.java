package productshop.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import productshop.domain.entities.Category;
import productshop.domain.models.binding.category.AddCategoryBindingModel;
import productshop.domain.models.binding.category.EditCategoryBindingModel;
import productshop.domain.models.view.category.ListCategoriesViewModel;
import productshop.domain.models.view.product.ListProductsViewModel;
import productshop.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean add(AddCategoryBindingModel model) {
        if (model == null || categoryRepository.findByName(model.getName()).isPresent()) {
            return false;
        }

        Category category = mapper.map(model, Category.class);
        categoryRepository.saveAndFlush(category);
        return true;
    }

    @Override
    public List<ListCategoriesViewModel> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(c -> mapper.map(c, ListCategoriesViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> T findById(String id, Class<T> targetClass) {
        return mapper.map(categoryRepository.findById(Long.parseLong(id)).orElseThrow(), targetClass);
    }

    @Override
    public boolean edit(EditCategoryBindingModel model) {
        if (model == null || categoryRepository.findByName(model.getName()).isPresent()) {
            return false;
        }

        Category category = categoryRepository.findById(model.getId()).orElseThrow();
        category.setName(model.getName());

        categoryRepository.saveAndFlush(category);
        return true;
    }

    @Override
    public void remove(String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow();
        categoryRepository.delete(category);
    }

    @Override
    public List<ListProductsViewModel> getProductsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findByIdEager(categoryId).orElseThrow();
        return category
                .getProducts()
                .stream()
                .map(p -> mapper.map(p, ListProductsViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
