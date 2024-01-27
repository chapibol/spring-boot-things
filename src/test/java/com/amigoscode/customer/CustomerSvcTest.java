package com.amigoscode.customer;

import com.amigoscode.exception.ResourceNotFoundException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerSvcTest {

    @Mock
    private CustomerDao customerDao;

    private CustomerSvc svcToTest;

    private static final Faker FAKER = new Faker();

    @BeforeEach
    void setUp() {
        svcToTest = new CustomerSvc(customerDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        svcToTest.getAllCustomers();

        verify(customerDao).getAllCustomers();
    }

    @Test
    void getCustomerById() {
        Long id = 1L;
        Customer ogCustomer = new Customer(id, "John Doe", "johndoe@example.com", 25);
        when(customerDao.getCustomerById(id))
                .thenReturn(Optional.of(ogCustomer));
        Customer customer = svcToTest.getCustomerById(id);
        verify(customerDao).getCustomerById(id);
        assertThat(customer).isEqualTo(ogCustomer);
    }

    @Test
    void getCustomerByIdNotFound() {
        Long id = 1L;
        when(customerDao.getCustomerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svcToTest.getCustomerById(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer %s could not be found.".formatted(id));
    }

    @Test
    void addCustomer() {
        String email = FAKER.internet().safeEmailAddress();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(FAKER.name().fullName(), email, 22);
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        svcToTest.addCustomer(request);
        verify(customerDao).insertCustomer(new Customer(request.name(), request.email(), request.age()));
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void testUpdateCustomer() {
    }

    @Test
    void isCustomerDataValid() {
    }

    @Test
    void toCustomer() {
    }
}