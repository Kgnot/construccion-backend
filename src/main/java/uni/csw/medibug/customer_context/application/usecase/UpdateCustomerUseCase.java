package uni.csw.medibug.customer_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.application.port.CustomerRepository;
import uni.csw.medibug.customer_context.application.usecase.command.UpdateCustomerCommand;
import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.error.CustomerNotFoundException;
import uni.csw.medibug.customer_context.domain.vo.BirthDay;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;
import uni.csw.medibug.customer_context.domain.vo.Document;
import uni.csw.medibug.customer_context.domain.vo.Email;
import uni.csw.medibug.shared.application.EventBus;

@Component
@Slf4j
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final EventBus eventBus;

    public UpdateCustomerUseCase(
            CustomerRepository customerRepository,
            EventBus eventBus) {
        this.customerRepository = customerRepository;
        this.eventBus = eventBus;
    }

    public void execute(UpdateCustomerCommand cmd) {
        if (cmd.customerId() == null || cmd.customerId().isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        Customer customer = customerRepository.findById(new CustomerId(cmd.customerId()));

        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with id: " + cmd.customerId());
        }

        Email email = Email.create(cmd.email());
        Document document = Document.create(cmd.documentType(), cmd.document());
        BirthDay birthDay = BirthDay.create(cmd.birthDay());

        Customer updatedCustomer = Customer.rehydrate(
                new CustomerId(cmd.customerId()),
                cmd.firstName(),
                cmd.lastName(),
                email,
                document,
                birthDay
        );

        var customerSaved = customerRepository.save(updatedCustomer);
        if (customerSaved != null) {
            log.info("Customer updated successfully with id: {}", cmd.customerId());
            return;
        }
        log.error("Customer not updated successfully");
    }
}

