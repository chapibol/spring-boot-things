package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(ogCustomer));
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
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
    }

    @Test
    void addCustomerEmailExistsError() {
        String email = FAKER.internet().safeEmailAddress();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(FAKER.name().fullName(), email, 22);
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> svcToTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // test that the line that should never execute actually does not ever execute
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        Long id = 1L;
        when(customerDao.existsCustomerById(id)).thenReturn(true);
        svcToTest.deleteCustomer(id);
        verify(customerDao).existsCustomerById(id);
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerThrowsError() {
        Long id = 1L;
        when(customerDao.existsCustomerById(id)).thenReturn(false);
        assertThatThrownBy(() -> svcToTest.deleteCustomer(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id %s could not be found.".formatted(id));
        verify(customerDao).existsCustomerById(id);
        verify(customerDao, never()).deleteCustomerById(anyLong());
    }

    @Test
    void updateCustomerNameChanged() {
        Long customerId = 1L;
        Customer ogCustomer = new Customer(customerId,"Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Mateo Messi"), Optional.of("lio@messi.com"), Optional.of(36));

        when(customerDao.getCustomerById(customerId)).thenReturn(Optional.of(ogCustomer));
        svcToTest.updateCustomer(customerId, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isEqualTo(customerId);
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name().get());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email().get());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age().get());
    }

    @Test
    void updateCustomerAgeChanged() {
        Long customerId = 1L;
        Customer ogCustomer = new Customer(customerId,"Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Lio Messi"), Optional.of("lio@messi.com"), Optional.of(26));

        when(customerDao.getCustomerById(customerId)).thenReturn(Optional.of(ogCustomer));
        svcToTest.updateCustomer(customerId, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isEqualTo(customerId);
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name().get());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email().get());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age().get());
    }

    @Test
    void updateCustomerEmailChanged() {
        Long customerId = 1L;
        Customer ogCustomer = new Customer(customerId,"Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Lio Messi"), Optional.of("lio10@messi.com"), Optional.of(36));

        when(customerDao.getCustomerById(customerId)).thenReturn(Optional.of(ogCustomer));
        svcToTest.updateCustomer(customerId, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isEqualTo(customerId);
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name().get());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email().get());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age().get());
    }

    @Test
    void updateCustomerNoChangesDetected() {
        Long customerId = 1L;
        Customer ogCustomer = new Customer(customerId,"Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty());

        when(customerDao.getCustomerById(customerId)).thenReturn(Optional.of(ogCustomer));
        assertThatThrownBy(() -> svcToTest.updateCustomer(customerId, updateRequest)).isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void updateCustomerNoChangesDetectedSameData() {
        Long customerId = 1L;
        Customer ogCustomer = new Customer(customerId,"Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Lio Messi"), Optional.of("lio@messi.com"), Optional.of(36));

        when(customerDao.getCustomerById(customerId)).thenReturn(Optional.of(ogCustomer));
        assertThatThrownBy(() -> svcToTest.updateCustomer(customerId, updateRequest)).isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void isCustomerDataValidNoChangedData() {
        Customer ogCustomer = new Customer("Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Lio Messi"), Optional.of("lio@messi.com"), Optional.of(36));

        assertThat(svcToTest.isCustomerDataValid(ogCustomer, updateRequest)).isFalse();
    }

    @Test
    void isCustomerDataValidNoChangedDataEmptyRequest() {
        Customer ogCustomer = new Customer("Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(svcToTest.isCustomerDataValid(ogCustomer, updateRequest)).isFalse();
    }

    @Test
    void isCustomerDataValidNameChanged() {
        Customer ogCustomer = new Customer("Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Mateo Messi"), Optional.of("lio@messi.com"), Optional.of(36));

        assertThat(svcToTest.isCustomerDataValid(ogCustomer, updateRequest)).isTrue();
    }

    @Test
    void isCustomerDataValidEmailChanged() {
        Customer ogCustomer = new Customer("Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Lio Messi"), Optional.of("lio10@messi.com"), Optional.of(36));

        assertThat(svcToTest.isCustomerDataValid(ogCustomer, updateRequest)).isTrue();
    }

    @Test
    void isCustomerDataValidNameAndAgeChanged() {
        Customer ogCustomer = new Customer("Lio Messi", "lio@messi.com", 36);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(Optional.of("Mateo Messi"), Optional.of("lio@messi.com"), Optional.of(15));

        assertThat(svcToTest.isCustomerDataValid(ogCustomer, updateRequest)).isTrue();
    }

    @Test
    void testToCustomer() {
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                FAKER.name().fullName(), FAKER.internet().safeEmailAddress(), 22);

        Customer mappedCustomer = svcToTest.toCustomer(customerRegistrationRequest);

        assertThat(mappedCustomer.getId()).isNull();
        assertThat(mappedCustomer.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(mappedCustomer.getEmail()).isEqualTo(customerRegistrationRequest.email());
        assertThat(mappedCustomer.getAge()).isEqualTo(customerRegistrationRequest.age());
    }
}