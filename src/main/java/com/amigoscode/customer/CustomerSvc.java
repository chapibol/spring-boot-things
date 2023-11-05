package com.amigoscode.customer;

import com.amigoscode.exception.ResourceNotFound;
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
                .orElseThrow(() -> new ResourceNotFound("customer %s could not be found.".formatted(id)));
    }
}