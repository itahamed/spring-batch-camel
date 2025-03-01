package com.batch.camel;

import com.batch.model.Product;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


public class ProductRouteBuilder extends RouteBuilder {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void configure() throws Exception {
        from("direct:processProducts")
            .log("Starting Camel route for Product data processing")
            .process(exchange -> {
                // Fetch products from database
                exchange.getIn().setBody(
                    jdbcTemplate.query(
                        "SELECT id, name, description, price, stock FROM products",
                        new BeanPropertyRowMapper<>(Product.class)
                    )
                );
            })
            .process(new ProductToBinFixedLengthConverter())
            .to("file:target?fileName=product-fixed-length.txt")
            .log("Product data processed and saved to fixed-length format");
    }
}