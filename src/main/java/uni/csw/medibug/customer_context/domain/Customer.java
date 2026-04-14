package uni.csw.medibug.customer_context.domain;

import lombok.Getter;
import uni.csw.medibug.customer_context.domain.vo.BirthDay;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;
import uni.csw.medibug.customer_context.domain.vo.Document;
import uni.csw.medibug.customer_context.domain.vo.Email;
import uni.csw.medibug.shared.domain.events.BaseDomainEvent;
import uni.csw.medibug.shared.domain.events.CustomerCreatedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Customer {

    private final CustomerId id;
    private final String firstName;
    private final String lastName;
    private final Email email;
    private final Document document;
    private final BirthDay birthDay;
    //eventos
    private final List<BaseDomainEvent> domainEvents;

    private Customer(
            CustomerId id,
            String firstName,
            String lastName,
            Email email,
            Document document,
            BirthDay birthDay
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.document = document;
        this.birthDay = birthDay;
        this.domainEvents = new ArrayList<>();
    }

    private Customer(
            String firstName,
            String lastName,
            Email email,
            Document document,
            BirthDay birthDay
    ) {
        this(
                null,
                firstName,
                lastName,
                email,
                document,
                birthDay
        );
    }

    public static Customer create(
            String firstName,
            String lastName,
            Email email,
            Document document,
            BirthDay birthDay
    ) {
        Customer customer = new Customer(firstName, lastName, email, document, birthDay);

        customer.record(new CustomerCreatedEvent(
                customer.getId() != null ? customer.getId().value() : null,
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail().getValue()
        ));

        return customer;
    }

    public static Customer rehydrate(
            CustomerId id,
            String firstName,
            String lastName,
            Email email,
            Document document,
            BirthDay birthDay
    ) {
        return new Customer(id, firstName, lastName, email, document, birthDay);
    }

    public List<BaseDomainEvent> pullDomainEvents() {
        var events = Collections.unmodifiableList(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    private void record(BaseDomainEvent event) {
        this.domainEvents.add(event);
    }
}