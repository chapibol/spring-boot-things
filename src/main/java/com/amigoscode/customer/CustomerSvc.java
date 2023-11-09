package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerSvc {

    private final CustomerDao customerDao;

    public CustomerSvc(@Qualifier("jpa") CustomerDao customerDaoSvc) {
        this.customerDao = customerDaoSvc;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.getAllCustomers();
    }

    public Customer getCustomerById(Integer id){
        return customerDao.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer %s could not be found.".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }
        customerDao.insertCustomer(toCustomer(customerRegistrationRequest));
    }

    public void deleteCustomer(Integer customerId){
        if(customerDao.existsCustomerById(customerId)){
            customerDao.deleteCustomerById(customerId);
        }else{
            throw new ResourceNotFoundException("customer with id %s could not be found.".formatted(customerId));
        }
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest){
        Customer customerToUpdate = this.getCustomerById(customerId);
        if(isCustomerDataValid(customerToUpdate, customerUpdateRequest)){
            customerDao.updateCustomer(updateCustomer(customerToUpdate, customerUpdateRequest));
        }else{
            throw new RequestValidationException("No data changes found");
        }
    }

    protected Customer updateCustomer(Customer customerToUpdate, CustomerUpdateRequest customerUpdateRequest){
        customerUpdateRequest.name()
                .filter(name -> !name.equals(customerToUpdate.getName()))
                .ifPresent(customerToUpdate::setName);

        customerUpdateRequest.email()
                .filter(email -> !email.equals(customerToUpdate.getEmail()))
                .ifPresent(customerToUpdate::setEmail);

        customerUpdateRequest.age()
                .filter(age -> !age.equals(customerToUpdate.getAge()))
                .ifPresent(customerToUpdate::setAge);

        return customerToUpdate;
    }


    /**
     * Method that will check that at least one of the fields provided is different from the customer to update object
     * @param customerToUpdate the customer to update
     * @param customerUpdateRequest the data about to customer to update
     * @return true if at least one piece of data is different, if no data is provided at all, that counts as not changes or if
     * data is provided, but it is not different from current data then this is also false.
     */

    public boolean isCustomerDataValid(Customer customerToUpdate, CustomerUpdateRequest customerUpdateRequest){
        return customerUpdateRequest.name().filter(name -> !customerToUpdate.getName().equals(name)).isPresent() ||
               customerUpdateRequest.email().filter(email -> !customerToUpdate.getEmail().equals(email)).isPresent() ||
        customerUpdateRequest.age().filter(age -> !customerToUpdate.getAge().equals(age)).isPresent();
    }

    protected Customer toCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        return new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(), customerRegistrationRequest.age());
    }
}