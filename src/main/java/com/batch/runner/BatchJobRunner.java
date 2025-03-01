package com.batch.runner;

import org.apache.camel.ProducerTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchJobRunner implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job customerJob;

    @Autowired
    private Job productJob;
    
    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Run both batch jobs
        runBatchJobs();
        
        // Then run Camel routes to process the data into fixed-length format
        runCamelRoutes();
    }
    
    private void runBatchJobs() throws Exception {
        // Run customer job
        JobParameters customerJobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(customerJob, customerJobParameters);

        // Run product job
        JobParameters productJobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis() + 1) // Add 1ms to make parameters unique
                .toJobParameters();
        jobLauncher.run(productJob, productJobParameters);
    }
    
    private void runCamelRoutes() {
        // Process customer data with Camel
        producerTemplate.sendBody("direct:processCustomers", null);
        
        // Process product data with Camel
        producerTemplate.sendBody("direct:processProducts", null);
    }
}
