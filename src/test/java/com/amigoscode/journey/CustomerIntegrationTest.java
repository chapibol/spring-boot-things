package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customer";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        Faker faker = new Faker();
        Name fakedName = faker.name();
        String fullName = fakedName.fullName();
        String email = fakedName.lastName() + "-" + UUID.randomUUID() + "@amgigoscode.com";
        int age = RANDOM.nextInt(1, 100);

        // create a registration request
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                fullName,
                email,
                age
        );
        // send a post request to our api
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get everyone from the api
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present from the request we sent
        Customer expectedCustomer = new Customer(fullName, email, age);
        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);
        // get customer by id from our api
        var customerId = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(customerId);

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        Faker faker = new Faker();
        Name fakedName = faker.name();
        String fullName = fakedName.fullName();
        String email = fakedName.lastName() + UUID.randomUUID() + "@amgigoscode.com";
        int age = RANDOM.nextInt(1, 100);

        // create a registration request
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                fullName,
                email,
                age
        );
        // send a post request to our api

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get everyone from the api
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id from our api
        var customerId = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        Faker faker = new Faker();
        Name fakedName = faker.name();
        String fullName = fakedName.fullName();
        String email = fakedName.lastName() + UUID.randomUUID() + "@amgigoscode.com";
        int age = RANDOM.nextInt(1, 100);

        // create a registration request
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                fullName,
                email,
                age
        );
        // send a post request to our api
        // insert a customer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get everyone from the api
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id from our api
        var customerId = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer
        Faker faker2 = new Faker();
        Name updatedFakedName = faker2.name();
        String updatedFullName = updatedFakedName.fullName();
        String updatedEmail = updatedFakedName.lastName() + UUID.randomUUID() + "@messi.com";
        CustomerRegistrationRequest updatedRequest = new CustomerRegistrationRequest(updatedFullName, updatedEmail, age);

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Customer expectedUpdatedCustomer = new Customer(customerId, updatedFullName, updatedEmail, age);
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedUpdatedCustomer);
    }
}