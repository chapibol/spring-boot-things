package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainersTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// disable embedded database for testing
class CustomerRepositoryTest extends AbstractTestcontainersTestConfig {
    // since this extends the AbstractTestcontainersTestConfig, this should make this test connect to testcontainers instead
    @Autowired
    private CustomerRepository repositoryToTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        String ogEmail = FAKER.internet().safeEmailAddress();
        Customer customerOne = new Customer(
                FAKER.name().fullName(),
                ogEmail,
                21
        );

        repositoryToTest.save(customerOne);

        assertThat(repositoryToTest.existsCustomerByEmail(ogEmail)).isTrue();
    }

    @Test
    void existsCustomerByEmailFalse() {
        assertThat(repositoryToTest.existsCustomerByEmail(FAKER.internet().safeEmailAddress())).isFalse();
    }

    @Test
    void existsCustomerById() {
        Customer customerOne = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                21
        );

        Customer savedCustomer = repositoryToTest.save(customerOne);

        assertThat(repositoryToTest.existsCustomerById(savedCustomer.getId())).isTrue();
    }

    @Test
    void existsCustomerByIdFalse() {
        assertThat(repositoryToTest.existsCustomerById(-1L)).isFalse();
    }
}