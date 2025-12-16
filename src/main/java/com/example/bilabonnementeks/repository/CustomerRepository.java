package com.example.bilabonnementeks.repository;

import com.example.bilabonnementeks.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    // henter alle kunder fra databasen og sorter efter nyeste først
    public List<Customer> findAll(){
        String sql = "SELECT * FROM customer ORDER BY customer_id DESC";
        return jdbcTemplate.query(sql, new CustomerMapper());
    }
    // finder en kunde ud far iD
    public Customer findById(int id) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        List<Customer> result = jdbcTemplate.query(sql, new CustomerMapper(), id);
        return result.isEmpty() ? null : result.get(0);
    }
    // henter alle kunder far databasen og sorter faldende ID
    public List<Customer> findAllOrderBy(String orderBy) {

        String order;

        switch (orderBy) {
            case "full_name DESC":
            case "customer_id DESC":
            case "customer_id ASC":
                order = orderBy;
                break;
            default:
                order = "full_name ASC";
        }
        // sortering her
        String sql = "SELECT * FROM customer ORDER BY " + order;
        return jdbcTemplate.query(sql, new CustomerMapper());
    }

    // finder kunder ud far email
    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        List<Customer> result = jdbcTemplate.query(sql, new CustomerMapper(), email);
        return result.isEmpty() ? null : result.get(0);
    }
    // oprettelse af nye kunde
    public void create(Customer c) {
        String sql = "INSERT INTO customer (full_name, email, phone_number, address) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, c.getFullName(), c.getEmail(), c.getPhoneNumber(), c.getAddress());
    }
    // sletter en kunde med bestemt ID
    public void deleteById(int id) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        jdbcTemplate.update(sql, id);
    }
    // mapper en række fra databasen til en objekt
    private static class CustomerMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer c = new Customer();
            c.setCustomerId(rs.getInt("customer_id"));
            c.setFullName(rs.getString("full_name"));
            c.setEmail(rs.getString("email"));
            c.setPhoneNumber(rs.getString("phone_number"));
            c.setAddress(rs.getString("address"));
            return c;
        }
    }
}
