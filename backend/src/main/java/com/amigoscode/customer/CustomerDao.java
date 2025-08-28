package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    void insertCustomer(Customer customer);

    void updateCustomer(Customer updatedCustomer);

    void deleteCustomerById(Long id);

    boolean existsCustomerById(Long id);

    boolean existsPersonWithEmail(String email);
}
