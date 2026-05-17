package uni.csw.medibug.customer_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.application.port.CustomerRepository;
import uni.csw.medibug.customer_context.application.usecase.dto.CustomerResponseDTO;
import uni.csw.medibug.customer_context.domain.Customer;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GetAllCustomersUseCase {

    private final CustomerRepository customerRepository;

    public GetAllCustomersUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDTO> execute() {
        List<Customer> customers = customerRepository.findAll();
        log.info("Retrieved {} customers", customers.size());
        return customers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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

