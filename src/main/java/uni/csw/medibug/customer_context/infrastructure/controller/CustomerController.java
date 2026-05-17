package uni.csw.medibug.customer_context.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.csw.medibug.customer_context.application.usecase.*;
import uni.csw.medibug.customer_context.application.usecase.command.DeleteCustomerCommand;
import uni.csw.medibug.customer_context.application.usecase.command.SaveCustomerCommand;
import uni.csw.medibug.customer_context.application.usecase.command.UpdateCustomerCommand;
import uni.csw.medibug.customer_context.application.usecase.dto.CustomerResponseDTO;
import uni.csw.medibug.shared.infrastructure.config.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final SaveCustomerUseCase saveCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final GetAllCustomersUseCase getAllCustomersUseCase;

    public CustomerController(
            SaveCustomerUseCase saveCustomerUseCase,
            GetCustomerUseCase getCustomerUseCase,
            UpdateCustomerUseCase updateCustomerUseCase,
            DeleteCustomerUseCase deleteCustomerUseCase,
            GetAllCustomersUseCase getAllCustomersUseCase
    ) {
        this.saveCustomerUseCase = saveCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.getAllCustomersUseCase = getAllCustomersUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody SaveCustomerCommand command) {
        saveCustomerUseCase.execute(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getById(@PathVariable String id) {
        CustomerResponseDTO customer = getCustomerUseCase.execute(id);
        return ResponseEntity
                .ok(ApiResponse.success(customer, "Customer retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getAll() {
        List<CustomerResponseDTO> customers = getAllCustomersUseCase.execute();
        return ResponseEntity
                .ok(ApiResponse.success(customers, "Customers retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String id,
            @RequestBody UpdateCustomerCommand command
    ) {
        UpdateCustomerCommand commandWithId = new UpdateCustomerCommand(
                id,
                command.firstName(),
                command.lastName(),
                command.email(),
                command.documentType(),
                command.document(),
                command.birthDay()
        );
        updateCustomerUseCase.execute(commandWithId);
        return ResponseEntity
                .ok(ApiResponse.success("Customer updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        deleteCustomerUseCase.execute(new DeleteCustomerCommand(id));
        return ResponseEntity
                .ok(ApiResponse.success("Customer deleted successfully"));
    }
}