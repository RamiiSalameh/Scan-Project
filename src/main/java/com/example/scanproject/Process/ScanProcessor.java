package com.example.scanproject.Process;
import com.example.scanproject.Data.Scan;
import com.example.scanproject.Data.ScanRepository;
import com.example.scanproject.Data.ScanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;

@Component
public class ScanProcessor {

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ScanRepository scanRepository;

    public ScanProcessor() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1); // Set the desired number of threads for processing
        taskExecutor.setMaxPoolSize(5); // Set the maximum number of threads for processing
        taskExecutor.setThreadNamePrefix("ScanProcessor-");
        taskExecutor.initialize();
    }

    public void processScan(Long scanId) {
        try {
            taskExecutor.execute(() -> {
                // Fetch the scan from the database
                Optional<Scan> scanOptional = scanRepository.findById(scanId);
                if (scanOptional.isPresent()) {
                    Scan scan = scanOptional.get();
                    // Perform the necessary processing tasks for the scan

                    // Update the scan status (e.g., mark it as running or complete)
                    scan.setStatus(ScanStatus.RUNNING);
                    scanRepository.save(scan);

                    // Simulate the scan process
                    try {
                        Thread.sleep(5000); // Simulating the scan process
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the scan status to complete
                    scan.setStatus(ScanStatus.COMPLETE);
                    scanRepository.save(scan);
                }
            });
        } catch (RejectedExecutionException e) {
            // Handle rejection if the task cannot be accepted by the thread pool
            // This can happen if the thread pool is full or has reached its maximum capacity
            // You can decide how to handle such scenarios based on your requirements
            e.printStackTrace();
        }
    }
}
