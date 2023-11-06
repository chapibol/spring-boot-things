package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
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
        try{
            customerDao.deleteCustomerById(customerId);
        }catch (Exception e){
            throw new ResourceNotFoundException("customer with %s could not be found".formatted(customerId));
        }
    }

    protected Customer toCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        return new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(), customerRegistrationRequest.age());
    }

}