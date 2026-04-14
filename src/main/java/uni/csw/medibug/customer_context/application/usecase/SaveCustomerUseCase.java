package uni.csw.medibug.customer_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.application.port.CustomerRepository;
import uni.csw.medibug.customer_context.application.usecase.command.SaveCustomerCommand;
import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.vo.BirthDay;
import uni.csw.medibug.customer_context.domain.vo.Document;
import uni.csw.medibug.customer_context.domain.vo.Email;
import uni.csw.medibug.shared.application.EventBus;

@Component
@Slf4j
public class SaveCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final EventBus eventBus;

    public SaveCustomerUseCase(
            CustomerRepository customerRepository,
            EventBus eventBus) {
        this.customerRepository = customerRepository;
        this.eventBus = eventBus;
    }

    public void execute(SaveCustomerCommand cmd) {
        // creamos los valueObjects para crear el Customer:
        Email email = Email.create(cmd.email());
        Document document = Document.create(cmd.documentType(), cmd.document());
        BirthDay birthDay = BirthDay.create(cmd.birthDay());

        // creamos el cliente
        Customer customer = Customer.create(
                cmd.firstName(),
                cmd.lastName(),
                email,
                document,
                birthDay
        );

        //creamos el usuario en el repository
        var customerSaved = customerRepository.save(customer);
        if (customerSaved != null) {
            var domainEvents = customer.pullDomainEvents();
            domainEvents.forEach(eventBus::publish);
            log.info("Customer saved successfully and evented");
            return;
        }
        log.error("Customer not saved successfully");
    }
}
