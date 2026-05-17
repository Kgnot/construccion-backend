package uni.csw.medibug.customer_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.application.port.CustomerRepository;
import uni.csw.medibug.customer_context.application.usecase.command.DeleteCustomerCommand;
import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.error.CustomerNotFoundException;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;

@Component
@Slf4j
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void execute(DeleteCustomerCommand cmd) {
        if (cmd.customerId() == null || cmd.customerId().isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        Customer customer = customerRepository.findById(new CustomerId(cmd.customerId()));

        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with id: " + cmd.customerId());
        }

        customerRepository.delete(new CustomerId(cmd.customerId()));
        log.info("Customer deleted successfully with id: {}", cmd.customerId());
    }
}

