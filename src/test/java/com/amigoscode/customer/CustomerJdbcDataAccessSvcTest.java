package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainersTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJdbcDataAccessSvcTest extends AbstractTestcontainersTestConfig {

    private CustomerJdbcDataAccessSvc serviceToTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        serviceToTest = new CustomerJdbcDataAccessSvc(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void getAllCustomers() {
        Customer customerOne = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                21
        );

        serviceToTest.insertCustomer(customerOne);
        List<Customer> allCustomers = serviceToTest.getAllCustomers();
        assertThat(allCustomers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customerOne = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                21
        );

        serviceToTest.insertCustomer(customerOne);
        Long idOfCustomer = getCustomerIdFromUniqueEmail(uniqueEmail);

        Optional<Customer> customerOpt = serviceToTest.getCustomerById(idOfCustomer);

        assertThat(customerOpt).isPresent().hasValueSatisfying(retrievedCustomer -> {
            assertThat(retrievedCustomer.getId()).isEqualTo(idOfCustomer);
            assertThat(retrievedCustomer.getName()).isEqualTo(customerOne.getName());
            assertThat(retrievedCustomer.getAge()).isEqualTo(customerOne.getAge());
            assertThat(retrievedCustomer.getEmail()).isEqualTo(customerOne.getEmail());
        });
    }

    @Test
    void getCustomerByIdEmptyResult() {
        Long wrongId = -1L;
        assertThat(serviceToTest.getCustomerById(wrongId)).isEmpty();
    }

    @Test
    void updateCustomerName() {
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                22
        );

        serviceToTest.insertCustomer(ogCustomer);
        Long customerId = getCustomerIdFromUniqueEmail(uniqueEmail);
        Customer customerToUpdate = serviceToTest.getCustomerById(customerId).orElseThrow();
        final String UPDATED_NAME = "Lionel Messi";
        customerToUpdate.setName(UPDATED_NAME);
        serviceToTest.updateCustomer(customerToUpdate);

        Customer updatedCustomerFromDb = serviceToTest.getCustomerById(customerId).orElseThrow();

        assertThat(updatedCustomerFromDb.getName()).isEqualTo(UPDATED_NAME);
        assertThat(updatedCustomerFromDb.getId()).isEqualTo(customerToUpdate.getId());
        assertThat(updatedCustomerFromDb.getAge()).isEqualTo(ogCustomer.getAge());
        assertThat(updatedCustomerFromDb.getEmail()).isEqualTo(ogCustomer.getEmail());
    }

    @Test
    void updateCustomerEmail() {
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                22
        );
        serviceToTest.insertCustomer(ogCustomer);
        Long customerId = getCustomerIdFromUniqueEmail(uniqueEmail);
        Customer customerToUpdate = serviceToTest.getCustomerById(customerId).orElseThrow();
        final String UPDATED_EMAIL = "lionel.messi@yahoo.com";
        customerToUpdate.setEmail(UPDATED_EMAIL);
        serviceToTest.updateCustomer(customerToUpdate);

        Customer updatedCustomerFromDb = serviceToTest.getCustomerById(customerId).orElseThrow();

        assertThat(updatedCustomerFromDb.getName()).isEqualTo(ogCustomer.getName());
        assertThat(updatedCustomerFromDb.getId()).isEqualTo(customerToUpdate.getId());
        assertThat(updatedCustomerFromDb.getAge()).isEqualTo(ogCustomer.getAge());
        assertThat(updatedCustomerFromDb.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void updateCustomerAge() {
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                22
        );
        serviceToTest.insertCustomer(ogCustomer);
        Long customerId = getCustomerIdFromUniqueEmail(uniqueEmail);
        Customer customerToUpdate = serviceToTest.getCustomerById(customerId).orElseThrow();
        final int UPDATED_AGE = 36;
        customerToUpdate.setAge(UPDATED_AGE);
        serviceToTest.updateCustomer(customerToUpdate);

        Customer updatedCustomerFromDb = serviceToTest.getCustomerById(customerId).orElseThrow();

        assertThat(updatedCustomerFromDb.getName()).isEqualTo(ogCustomer.getName());
        assertThat(updatedCustomerFromDb.getId()).isEqualTo(customerToUpdate.getId());
        assertThat(updatedCustomerFromDb.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(updatedCustomerFromDb.getEmail()).isEqualTo(customerToUpdate.getEmail());
    }

    @Test
    void updateAllCustomerProperties(){
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                25
        );
        serviceToTest.insertCustomer(ogCustomer);
        Long customerId = getCustomerIdFromUniqueEmail(uniqueEmail);
        Customer customerToUpdate = serviceToTest.getCustomerById(customerId).orElseThrow();
        // assert before updates
        assertThat(customerToUpdate.getName()).isEqualTo(ogCustomer.getName());
        assertThat(customerToUpdate.getEmail()).isEqualTo(ogCustomer.getEmail());
        assertThat(customerToUpdate.getAge()).isEqualTo(ogCustomer.getAge());
        final String NEW_NAME = "Lionel Messi";
        final String NEW_EMAIL = "thegoat@gmail.com";
        final int NEW_AGE = 36;
        customerToUpdate.setName(NEW_NAME);
        customerToUpdate.setEmail(NEW_EMAIL);
        customerToUpdate.setAge(NEW_AGE);

        serviceToTest.updateCustomer(customerToUpdate);
        Customer updatedCustomerFromDb = serviceToTest.getCustomerById(customerId).orElseThrow();

        assertThat(updatedCustomerFromDb.getName()).isEqualTo(NEW_NAME);
        assertThat(updatedCustomerFromDb.getEmail()).isEqualTo(NEW_EMAIL);
        assertThat(updatedCustomerFromDb.getAge()).isEqualTo(NEW_AGE);
    }

    @Test
    void updateAllCustomersNoUpdate(){
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                29
        );
        serviceToTest.insertCustomer(ogCustomer);
        Long customerId = getCustomerIdFromUniqueEmail(uniqueEmail);
        Customer customerToUpdate = serviceToTest.getCustomerById(customerId).orElseThrow();
        serviceToTest.updateCustomer(customerToUpdate);
        Customer nonUpdatedCustomer = serviceToTest.getCustomerById(customerId).orElseThrow();
        assertThat(nonUpdatedCustomer.getName()).isEqualTo(ogCustomer.getName());
        assertThat(nonUpdatedCustomer.getEmail()).isEqualTo(ogCustomer.getEmail());
        assertThat(nonUpdatedCustomer.getAge()).isEqualTo(ogCustomer.getAge());
    }

    @Test
    void existsPersonWithEmail() {
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                22
        );

        serviceToTest.insertCustomer(ogCustomer);

        assertThat(serviceToTest.existsPersonWithEmail(ogCustomer.getEmail())).isTrue();
    }

    @Test
    void personWithEmailDoesNotExist() {
        assertThat(serviceToTest.existsPersonWithEmail("someRandoEmail@email.com")).isFalse();
    }

    /**
     * Helper method to get a customer's id using a unique email
     * @param uniqueEmail the email to search by
     * @return the id of the customer in Long form
     */
    private Long getCustomerIdFromUniqueEmail(String uniqueEmail){
        return serviceToTest.getAllCustomers().stream()
                .filter(customer -> customer.getEmail().equals(uniqueEmail))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
    }

    @Test
    void existsCustomerById() {
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                30
        );
        serviceToTest.insertCustomer(ogCustomer);
        assertThat(serviceToTest.existsCustomerById(getCustomerIdFromUniqueEmail(uniqueEmail))).isTrue();
    }

    @Test
    void existsCustomerByIdFalse() {
       assertThat(serviceToTest.existsCustomerById(-1L)).isFalse();
    }

    @Test
    void deleteCustomerById() {
        String uniqueEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer ogCustomer = new Customer(
                FAKER.name().fullName(),
                uniqueEmail,
                25
        );
        serviceToTest.insertCustomer(ogCustomer);
        Long customerId = getCustomerIdFromUniqueEmail(uniqueEmail);
        assertThat(serviceToTest.getCustomerById(customerId)).isNotEmpty();
        serviceToTest.deleteCustomerById(customerId);
        assertThat(serviceToTest.getCustomerById(customerId)).isEmpty();
    }
}