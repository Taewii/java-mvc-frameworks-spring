package productshop.services;

import productshop.domain.models.binding.product.AddProductBindingModel;
import productshop.domain.models.binding.product.DeleteProductBindingModel;
import productshop.domain.models.binding.product.EditProductBindingModel;
import productshop.domain.models.view.product.ListProductsViewModel;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    String add(AddProductBindingModel model);

    <T> T findById(UUID id, Class<T> targetClass);

    List<ListProductsViewModel> findAll();

    void edit(EditProductBindingModel model);

    void delete(DeleteProductBindingModel model);
}
