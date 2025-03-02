package com.batch.config;

import javax.sql.DataSource;

import com.batch.camel.CamelItemWriter;
import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Customer;
import com.batch.model.FormattedCustomer;
import com.batch.processor.CustomerProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CamelItemWriter<FormattedCustomer> camelItemWriter;

    public BatchConfiguration(DataSource dataSource, JobRepository jobRepository, 
                             PlatformTransactionManager transactionManager,
                             CamelItemWriter<FormattedCustomer> camelItemWriter) {
        this.dataSource = dataSource;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.camelItemWriter = camelItemWriter;
    }

    @Bean
    public JdbcCursorItemReader<Customer> reader() {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("customerReader")
                .dataSource(dataSource)
                .sql("SELECT id, first_name, last_name, email, phone_number FROM customers")
                .rowMapper(customerRowMapper())
                .build();
    }

    @Bean
    public RowMapper<Customer> customerRowMapper() {
        return (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhoneNumber(rs.getString("phone_number"));
            return customer;
        };
    }

    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }

    @Bean
    public Step processCustomersStep() {
        return new StepBuilder("processCustomersStep", jobRepository)
                .<Customer, FormattedCustomer>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(camelItemWriter)
                .build();
    }

    @Bean
    public Job exportCustomerJob(JobCompletionNotificationListener listener, Step processCustomersStep) {
        return new JobBuilder("exportCustomerJob", jobRepository)
                .listener(listener)
                .start(processCustomersStep)
                .build();
    }
}