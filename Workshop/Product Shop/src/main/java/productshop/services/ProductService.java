package productshop.services;

import productshop.domain.models.binding.AddProductBindingModel;

public interface ProductService {

    boolean add(AddProductBindingModel model);
}
