package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
public class Main {
    public static void main(String [] args){
       SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Customer customerOne = new Customer(faker.name().firstName(), faker.name().lastName() + "@gmail.com", faker.number().numberBetween(18, 45));
            Customer customerTwo = new Customer(faker.name().firstName(), faker.name().lastName() + "@gmail.com", faker.number().numberBetween(18, 45));
            customerRepository.saveAll(List.of(customerOne, customerTwo));
        };
    }
}