package uni.csw.medibug.customer_context.application.port;

import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;

public interface CustomerRepository {

    Customer findById(CustomerId id);

    Customer save(Customer customer);

}
