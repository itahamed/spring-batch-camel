package com.batch.config;

import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Product;
import com.batch.processor.ProductItemProcessor;
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
public class BatchConfigurationJob2 {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public JdbcCursorItemReader<Product> productReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Product>()
                .name("productReader")
                .dataSource(dataSource)
                .sql("SELECT id, name, description, price, stock FROM products")
                .rowMapper((rs, rowNum) -> {
                    Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setStock(rs.getInt("stock"));
                    return product;
                })
                .build();
    }

    @Bean
    public ProductItemProcessor productProcessor() {
        return new ProductItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Product> productWriter() {
        return new FlatFileItemWriterBuilder<Product>()
                .name("productWriter")
                .resource(new FileSystemResource("target/product-output.csv"))
                .delimited()
                .delimiter(",")
                .names("id", "name", "description", "price", "stock")
                .build();
    }

    @Bean
    public Step productStep(JdbcCursorItemReader<Product> productReader) {
        return new StepBuilder("productStep", jobRepository)
                .<Product, Product>chunk(10, transactionManager)
                .reader(productReader)
                .processor(productProcessor())
                .writer(productWriter())
                .build();
    }

    @Bean
    public Job productJob(JobCompletionNotificationListener listener, Step productStep) {
        return new JobBuilder("productJob", jobRepository)
                .listener(listener)
                .start(productStep)
                .build();
    }
}