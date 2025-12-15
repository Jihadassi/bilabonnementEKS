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

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getAllCustomersOrderBy(String orderBy) {
        return customerRepository.findAllOrderBy(orderBy);
    }

    public Customer findById(int id) {
        return customerRepository.findById(id);
    }

    public void createCustomer(Customer customer) {
        customerRepository.create(customer);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
}
