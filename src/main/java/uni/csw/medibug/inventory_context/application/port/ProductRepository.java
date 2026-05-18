package uni.csw.medibug.inventory_context.application.port;

import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(ProductId id);

    Product save(Product product);

    List<Product> findAll();

    void delete(ProductId id);

}

