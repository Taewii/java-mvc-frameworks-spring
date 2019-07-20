package productshop.services;

import productshop.domain.models.binding.AddCategoryBindingModel;
import productshop.domain.models.binding.EditCategoryBindingModel;
import productshop.domain.models.view.ListCategoriesViewModel;
import productshop.domain.models.view.ListProductsViewModel;

import java.util.List;

public interface CategoryService {

    boolean add(AddCategoryBindingModel model);

    List<ListCategoriesViewModel> findAll();

    <T> T findById(String id, Class<T> targetClass);

    boolean edit(EditCategoryBindingModel model);

    void remove(String categoryName);

    List<ListProductsViewModel> getProductsByCategoryId(Long categoryId);
}
