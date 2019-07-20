package productshop.services;

import productshop.domain.models.binding.AddProductBindingModel;
import productshop.domain.models.binding.DeleteProductBindingModel;
import productshop.domain.models.binding.EditProductBindingModel;
import productshop.domain.models.view.ListProductsViewModel;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    String add(AddProductBindingModel model);

    <T> T findById(UUID id, Class<T> targetClass);

    List<ListProductsViewModel> findAll();

    void edit(EditProductBindingModel model);

    void delete(DeleteProductBindingModel model);
}
