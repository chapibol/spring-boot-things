package com.amigoscode.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerSvc customerSvc;

    public CustomerController(CustomerSvc customerSvc) {
        this.customerSvc = customerSvc;
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> allCustomers(){
        return ResponseEntity.ok(customerSvc.getAllCustomers());
    }

    @GetMapping("{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(customerSvc.getCustomerById(customerId));
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerSvc.addCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable Long customerId){
        customerSvc.deleteCustomer(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest){
        customerSvc.updateCustomer(customerId, customerUpdateRequest);
    }
}