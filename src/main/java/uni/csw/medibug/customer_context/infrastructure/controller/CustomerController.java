package uni.csw.medibug.customer_context.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.csw.medibug.customer_context.application.usecase.SaveCustomerUseCase;
import uni.csw.medibug.customer_context.application.usecase.command.SaveCustomerCommand;
import uni.csw.medibug.shared.infrastructure.config.ApiResponse;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final SaveCustomerUseCase saveCustomerUseCase;

    public CustomerController(SaveCustomerUseCase saveCustomerUseCase) {
        this.saveCustomerUseCase = saveCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody SaveCustomerCommand command) {
        saveCustomerUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully"));
    }
}