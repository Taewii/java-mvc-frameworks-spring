package productshop.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.models.binding.product.AddProductBindingModel;
import productshop.domain.models.binding.product.DeleteProductBindingModel;
import productshop.domain.models.binding.product.EditProductBindingModel;
import productshop.domain.models.view.product.ListProductsViewModel;
import productshop.repositories.CategoryRepository;
import productshop.repositories.ProductRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GoogleDriveService googleDriveService;
    private final ModelMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              GoogleDriveService googleDriveService,
                              ModelMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.googleDriveService = googleDriveService;
        this.mapper = mapper;
    }

    @Override
    public String add(AddProductBindingModel model) {
        String fileId;
        try {
            fileId = googleDriveService.uploadFile(model.getImage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }

        Product product = mapper.map(model, Product.class);
        product.setImageUrl(fileId);
        product.getCategories().clear(); // has one null value for some reason
        product.setCategories(this.getCategoriesFromValues(model.getCategories()));

        Product entity = productRepository.saveAndFlush(product);
        return entity.getId().toString();
    }

    @Override
    public <T> T findById(UUID id, Class<T> targetClass) {
        return mapper.map(productRepository.findByIdEager(id).orElseThrow(), targetClass);
    }

    @Override
    public List<ListProductsViewModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(p -> mapper.map(p, ListProductsViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void edit(EditProductBindingModel model) {
        Product product = productRepository.findByIdEager(model.getId()).orElseThrow();
        product.setName(model.getName());
        product.setDescription(model.getDescription());
        product.setPrice(model.getPrice());
        product.getCategories().addAll(this.getCategoriesFromValues(model.getCategories()));

        productRepository.saveAndFlush(product);
    }

    @Override
    public void delete(DeleteProductBindingModel model) {
        Product product = productRepository.findById(model.getId()).orElseThrow();
        googleDriveService.delete(product.getImageUrl());
        productRepository.delete(product);
    }

    private Set<Category> getCategoriesFromValues(Collection<Long> values) {
        return values
                .stream()
                .map(v -> categoryRepository.findById(v).orElseThrow())
                .collect(Collectors.toSet());
    }
}
