package com.batch.camel;

import com.batch.model.Customer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.nio.charset.StandardCharsets;
import java.util.List;

public class CustomerToBinFixedLengthConverter implements Processor {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerToBinFixedLengthConverter.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        List<Customer> customers = exchange.getIn().getBody(List.class);
        StringBuilder fixedLengthBuilder = new StringBuilder();
        
        for (Customer customer : customers) {
            // Create a fixed-length record:
            // ID: 10 chars, First Name: 20 chars, Last Name: 20 chars, Email: 50 chars
            String fixedLengthRecord = 
                String.format("%-10s", customer.getId()) +
                String.format("%-20s", customer.getFirstName()) +
                String.format("%-20s", customer.getLastName()) +
                String.format("%-50s", customer.getEmail());
            
            fixedLengthBuilder.append(fixedLengthRecord).append("\n");
            log.info("Converted customer to fixed length: {}", fixedLengthRecord);
        }
        
        exchange.getIn().setBody(fixedLengthBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
