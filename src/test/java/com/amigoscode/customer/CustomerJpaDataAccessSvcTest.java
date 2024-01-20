package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


class CustomerJpaDataAccessSvcTest {

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable autoCloseable;

    private CustomerJpaDataAccessSvc svcToTest;

    @BeforeEach
    void setUp() {
        // important to open the resource before every test
        autoCloseable = MockitoAnnotations.openMocks(this);
        svcToTest = new CustomerJpaDataAccessSvc(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        // important so that after each test, we have a fresh mock to work with.
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        // When
        svcToTest.getAllCustomers();
        // Then. checking that the findAll method within the customerRepository is called
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        // Given
        Long id = 1L;
        // here we just need to check that the id passed equals the id with which the call to findById is made
        // When
        svcToTest.getCustomerById(id);

        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
    }

    @Test
    void deleteCustomerById() {
    }

    @Test
    void existsCustomerById() {
    }

    @Test
    void existsPersonWithEmail() {
    }

    @Test
    void updateCustomer() {
    }
}