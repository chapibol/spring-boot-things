package com.amigoscode.customer;

import org.springframework.dao.EmptyResultDataAccessException;
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
                SELECT id, name, email, age FROM Customer WHERE id=?
                """;
        Customer foundCustomer = null;
        try{
           foundCustomer = jdbcTemplate.queryForObject(sql, customerRowMapper, id);
        }catch (EmptyResultDataAccessException ex){
            System.out.println(ex.getMessage() + " for id=" + id);
        }
        return Optional.ofNullable(foundCustomer);
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

    }

    @Override// TODO Implement
    public void deleteCustomerById(Long id) {

    }

    @Override// TODO Implement
    public boolean existsCustomerById(Long id) {
        return false;
    }

    @Override // TODO Implement this also
    public boolean existsPersonWithEmail(String email) {
        return false;
    }
}
