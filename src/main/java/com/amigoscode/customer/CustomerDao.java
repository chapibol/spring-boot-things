package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Integer id);

    void insertCustomer(Customer customer);

    void updateCustomer(Customer updatedCustomer);

    void deleteCustomerById(Integer id);

    boolean existsCustomerById(Integer id);

    boolean existsPersonWithEmail(String email);
}
