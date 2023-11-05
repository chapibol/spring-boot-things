package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessSvc implements CustomerDao {

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 21);
        customers.add(alex);
        Customer jamila = new Customer(2, "Jamila", "jamila@gmail.com", 19);
        customers.add(jamila);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }
}