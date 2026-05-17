package uni.csw.medibug.customer_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.application.port.CustomerRepository;
import uni.csw.medibug.customer_context.application.usecase.dto.CustomerResponseDTO;
import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.error.CustomerNotFoundException;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;

@Component
@Slf4j
public class GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDTO execute(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        Customer customer = customerRepository.findById(new CustomerId(customerId));

        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }

        return mapToDTO(customer);
    }

    private CustomerResponseDTO mapToDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId().value(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail() != null ? customer.getEmail().getValue() : null,
                customer.getDocument() != null ? customer.getDocument().getDocumentType().name() : null,
                customer.getDocument() != null ? customer.getDocument().getValue() : null,
                customer.getBirthDay() != null ? customer.getBirthDay().getValue() : null
        );
    }
}

