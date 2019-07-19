package productshop.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.models.binding.AddProductBindingModel;
import productshop.repositories.CategoryRepository;
import productshop.repositories.ProductRepository;

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
    public boolean add(AddProductBindingModel model) {
        String fileId;
        try {
            fileId = googleDriveService.uploadFile(model.getImage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return false;
        }

        Product product = mapper.map(model, Product.class);
        product.setImageUrl(fileId);
        product.getCategories().clear(); // has one null value for some reason
        model.getCategories().forEach(categoryId -> {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            product.getCategories().add(category);
        });

        productRepository.saveAndFlush(product);
        return true;
    }
}
