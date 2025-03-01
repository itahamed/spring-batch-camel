package com.batch.processor;

import com.batch.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger log = LoggerFactory.getLogger(CustomerItemProcessor.class);

    @Override
    public Customer process(final Customer customer) {
        final String firstName = customer.getFirstName().toUpperCase();
        final String lastName = customer.getLastName().toUpperCase();
        final String email = customer.getEmail().toLowerCase();

        final Customer transformedCustomer = new Customer(customer.getId(), firstName, lastName, email);

        log.info("Converting (" + customer + ") into (" + transformedCustomer + ")");

        return transformedCustomer;
    }
}