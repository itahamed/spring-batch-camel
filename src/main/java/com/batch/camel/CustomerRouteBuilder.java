package com.batch.camel;

import com.batch.model.Customer;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


public class CustomerRouteBuilder extends RouteBuilder {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void configure() throws Exception {
        from("direct:processCustomers")
            .log("Starting Camel route for Customer data processing")
            .process(exchange -> {
                // Fetch customers from database
                exchange.getIn().setBody(
                    jdbcTemplate.query(
                        "SELECT id, first_name as firstName, last_name as lastName, email FROM customers",
                        new BeanPropertyRowMapper<>(Customer.class)
                    )
                );
            })
            .process(new CustomerToBinFixedLengthConverter())
            .to("file:target?fileName=customer-fixed-length.txt")
            .log("Customer data processed and saved to fixed-length format");
    }
}