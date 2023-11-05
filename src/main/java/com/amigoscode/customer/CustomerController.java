package com.amigoscode.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class CustomerController {

    private final CustomerSvc customerSvc;

    public CustomerController(CustomerSvc customerSvc) {
        this.customerSvc = customerSvc;
    }

    @GetMapping("/api/v1/customer")
    public ResponseEntity<List<Customer>> allCustomers(){
        return ResponseEntity.ok(customerSvc.getAllCustomers());
    }

    @GetMapping("/api/v1/customer/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer customerId){
        return ResponseEntity.ok(customerSvc.getCustomerById(customerId));
    }
}