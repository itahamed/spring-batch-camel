package com.batch.controller;


import com.batch.runner.BatchJobRunner;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchJobController {

    @Autowired
    private BatchJobRunner batchJobRunner;

    @GetMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        try {
            batchJobRunner.run();
            return ResponseEntity.ok("Batch job started successfully.");
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | JobExecutionNotRunningException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start batch job: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}