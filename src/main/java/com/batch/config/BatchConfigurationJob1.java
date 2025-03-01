package com.batch.config;

import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Customer;
import com.batch.processor.CustomerItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfigurationJob1 {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public JdbcCursorItemReader<Customer> customerReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("customerReader")
                .dataSource(dataSource)
                .sql("SELECT id, first_name, last_name, email FROM customers")
                .rowMapper((rs, rowNum) -> {
                    Customer customer = new Customer();
                    customer.setId(rs.getLong("id"));
                    customer.setFirstName(rs.getString("first_name"));
                    customer.setLastName(rs.getString("last_name"));
                    customer.setEmail(rs.getString("email"));
                    return customer;
                })
                .build();
    }

    @Bean
    public CustomerItemProcessor customerProcessor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Customer> customerWriter() {
        return new FlatFileItemWriterBuilder<Customer>()
                .name("customerWriter")
                .resource(new FileSystemResource("target/customer-output.csv"))
                .delimited()
                .delimiter(",")
                .names("id", "firstName", "lastName", "email")
                .build();
    }

    @Bean
    public Step customerStep(JdbcCursorItemReader<Customer> customerReader) {
        return new StepBuilder("customerStep", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(customerReader)
                .processor(customerProcessor())
                .writer(customerWriter())
                .build();
    }

    @Bean
    public Job customerJob(JobCompletionNotificationListener listener, Step customerStep) {
        return new JobBuilder("customerJob", jobRepository)
                .listener(listener)
                .start(customerStep)
                .build();
    }
}
