package uni.csw.medibug.customer_context.application.port;

import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;

import java.util.List;

public interface CustomerRepository {

    Customer findById(CustomerId id);

    Customer save(Customer customer);

    List<Customer> findAll();

    void delete(CustomerId id);

}
