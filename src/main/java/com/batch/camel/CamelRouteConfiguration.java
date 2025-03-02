package com.batch.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.fixed.BindyFixedLengthDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CamelRouteConfiguration extends RouteBuilder {

    @Value("${output.directory}")
    private String outputDirectory;

    @Override
    public void configure() throws Exception {
        BindyFixedLengthDataFormat bindy = new BindyFixedLengthDataFormat(FormattedCustomerRecord.class);
        
        // Route for processing customers and writing to fixed-length file
        from("direct:processCustomer")
            .routeId("customerToFixedLengthFile")
            .log("Processing customer: ${body}")
            .bean(FormattedCustomerConverter.class)
            .marshal(bindy)
            .to("file:" + outputDirectory + "?fileName=customers.txt&fileExist=append")
            .log("Customer written to file");
    }
}