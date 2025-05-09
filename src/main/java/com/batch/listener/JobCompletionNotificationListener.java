package com.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("!!! JOB {} STARTING! Time: {}", 
                 jobExecution.getJobInstance().getJobName(),
                 jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB {} FINISHED! Time: {}", 
                     jobExecution.getJobInstance().getJobName(),
                     jobExecution.getEndTime());
            
            String jobName = jobExecution.getJobInstance().getJobName();
            
            if ("customerJob".equals(jobName)) {
                log.info("!!! Customers processed:");
                jdbcTemplate.query("SELECT id, first_name, last_name, email FROM customers",
                    (rs, row) -> new String(
                        rs.getLong(1) + " " + 
                        rs.getString(2) + " " + 
                        rs.getString(3) + " " +
                        rs.getString(4))
                ).forEach(log::info);
            } else if ("productJob".equals(jobName)) {
                log.info("!!! Products processed:");
                jdbcTemplate.query("SELECT id, name, price, stock FROM products",
                    (rs, row) -> new String(
                        rs.getLong(1) + " " + 
                        rs.getString(2) + " " + 
                        rs.getBigDecimal(3) + " " +
                        rs.getInt(4))
                ).forEach(log::info);
            }
        }
        // Terminate the application
      //  System.exit(0);
    }
}