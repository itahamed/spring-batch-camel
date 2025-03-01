package com.batch.camel;

import com.batch.model.Product;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductToBinFixedLengthConverter implements Processor {
    
    private static final Logger log = LoggerFactory.getLogger(ProductToBinFixedLengthConverter.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        List<Product> products = exchange.getIn().getBody(List.class);
        StringBuilder fixedLengthBuilder = new StringBuilder();
        
        for (Product product : products) {
            // Create a fixed-length record:
            // ID: 10 chars, Name: 30 chars, Description: 100 chars, Price: 15 chars, Stock: 10 chars
            String fixedLengthRecord = 
                String.format("%-10s", product.getId()) +
                String.format("%-30s", product.getName()) +
                String.format("%-100s", product.getDescription()) +
                String.format("%-15s", product.getPrice().toString()) +
                String.format("%-10s", product.getStock());
            
            fixedLengthBuilder.append(fixedLengthRecord).append("\n");
            log.info("Converted product to fixed length: {}", fixedLengthRecord);
        }
        
        exchange.getIn().setBody(fixedLengthBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
