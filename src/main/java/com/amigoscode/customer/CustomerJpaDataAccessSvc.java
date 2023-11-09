package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDataAccessSvc implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessSvc(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
        customerRepository.save(updatedCustomer);
    }
}
