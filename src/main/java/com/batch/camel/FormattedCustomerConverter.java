package com.batch.camel;

import com.batch.model.FormattedCustomer;
import org.springframework.stereotype.Component;

@Component
public class FormattedCustomerConverter {

    public FormattedCustomerRecord convert(FormattedCustomer customer) {
        FormattedCustomerRecord record = new FormattedCustomerRecord();
        record.setId(customer.getId());
        record.setFirstName(customer.getFirstName());
        record.setLastName(customer.getLastName());
        record.setEmail(customer.getEmail());
        record.setPhoneNumber(customer.getPhoneNumber());
        return record;
    }
}