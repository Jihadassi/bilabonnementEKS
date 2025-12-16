package com.example.bilabonnementeks.service;



import com.example.bilabonnementeks.repository.CustomerRepository;
import com.example.bilabonnementeks.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
// henter alle kunder
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
// henter alle kunder fra databasen med specifik sortering
    public List<Customer> getAllCustomersOrderBy(String orderBy) {
        return customerRepository.findAllOrderBy(orderBy);
    }
// finder en kunde ud fra ID
    public Customer findById(int id) {
        return customerRepository.findById(id);
    }
// opretter en ny kunde
    public void createCustomer(Customer customer) {
        customerRepository.create(customer);
    }
// finder en kunde ud fra email
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
//sletter en kunde ud fra ID
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
}
