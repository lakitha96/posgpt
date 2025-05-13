package com.example.posgpt.service;

import com.example.posgpt.model.Customer;
import com.example.posgpt.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author lakithaprabudh
 */
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public List<Customer> getCustomersByLoyaltyLevel(String loyaltyLevel) {
        return customerRepository.findByLoyaltyLevel(loyaltyLevel);
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer updateLoyaltyPoints(Long customerId, int pointsChange) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        int newPoints = customer.getLoyaltyPoints() + pointsChange;
        customer.setLoyaltyPoints(newPoints);

        // Update loyalty level based on points
        if (newPoints >= 1000) {
            customer.setLoyaltyLevel("PLATINUM");
        } else if (newPoints >= 500) {
            customer.setLoyaltyLevel("GOLD");
        } else if (newPoints >= 100) {
            customer.setLoyaltyLevel("SILVER");
        } else {
            customer.setLoyaltyLevel("STANDARD");
        }

        return customerRepository.save(customer);
    }
}
