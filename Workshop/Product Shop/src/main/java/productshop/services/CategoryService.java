package productshop.services;

import productshop.domain.models.binding.category.AddCategoryBindingModel;
import productshop.domain.models.binding.category.EditCategoryBindingModel;
import productshop.domain.models.view.category.ListCategoriesViewModel;
import productshop.domain.models.view.product.ListProductsViewModel;

import java.util.List;

public interface CategoryService {

    boolean add(AddCategoryBindingModel model);

    List<ListCategoriesViewModel> findAll();

    <T> T findById(String id, Class<T> targetClass);

    boolean edit(EditCategoryBindingModel model);

    void remove(String categoryName);

    List<ListProductsViewModel> getProductsByCategoryId(Long categoryId);
}
