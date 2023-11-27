package com.amigoscode.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJdbcDataAccessSvc implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDataAccessSvc(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getAllCustomers() {
        var sql =  """
                SELECT id, name, email, age FROM Customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        var sql = """
                SELECT id, name, email, age FROM Customer WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES(?, ?, ?)
                """;
        int numRowsAffected = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("jdbcTemplate.update = " + numRowsAffected);
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
        var sql = """
                UPDATE Customer
                SET name = coalesce(?, name),
                    email = coalesce(?, email),
                    age = coalesce(?, age)
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,updatedCustomer.getName(), updatedCustomer.getEmail(), updatedCustomer.getAge(), updatedCustomer.getId());
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE
                FROM Customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsCustomerById(Long id) {
        var sql = """
                SELECT count(id)
                FROM Customer
                WHERE id = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM Customer
                WHERE email = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }
}
