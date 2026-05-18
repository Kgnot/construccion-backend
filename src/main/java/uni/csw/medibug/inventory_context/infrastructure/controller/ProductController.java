package uni.csw.medibug.inventory_context.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.csw.medibug.inventory_context.application.usecase.*;
import uni.csw.medibug.inventory_context.application.usecase.command.DeleteProductCommand;
import uni.csw.medibug.inventory_context.application.usecase.command.SaveProductCommand;
import uni.csw.medibug.inventory_context.application.usecase.command.UpdateProductCommand;
import uni.csw.medibug.inventory_context.application.usecase.dto.ProductResponseDTO;
import uni.csw.medibug.shared.infrastructure.config.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final SaveProductUseCase saveProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;

    public ProductController(
            SaveProductUseCase saveProductUseCase,
            GetProductUseCase getProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase,
            GetAllProductsUseCase getAllProductsUseCase
    ) {
        this.saveProductUseCase = saveProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody SaveProductCommand command) {

        var fullCommand = new SaveProductCommand(
                command.name(),
                command.description(),
                command.serialNumber(),
                command.model(),
                command.manufacturer(),
                command.status(),
                command.userId(),
                command.deviceType(),
                command.interval()
        );
        saveProductUseCase.execute(fullCommand);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getById(@PathVariable String id) {
        ProductResponseDTO product = getProductUseCase.execute(id);
        return ResponseEntity
                .ok(ApiResponse.success(product, "Product retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAll() {
        List<ProductResponseDTO> products = getAllProductsUseCase.execute();
        return ResponseEntity
                .ok(ApiResponse.success(products, "Products retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String id,
            @RequestBody UpdateProductCommand command) {
        updateProductUseCase.execute(new UpdateProductCommand(
                id,
                command.name(),
                command.description(),
                command.serialNumber(),
                command.model(),
                command.manufacturer(),
                command.status()
        ));
        return ResponseEntity
                .ok(ApiResponse.success("Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        deleteProductUseCase.execute(new DeleteProductCommand(id));
        return ResponseEntity
                .ok(ApiResponse.success("Product deleted successfully"));
    }
}

