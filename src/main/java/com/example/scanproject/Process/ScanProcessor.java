package com.example.scanproject.Process;
import com.example.scanproject.Data.Scan;
import com.example.scanproject.Data.ScanRepository;
import com.example.scanproject.Data.ScanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

@Component
public class ScanProcessor {

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ScanRepository scanRepository;

    private int poolSize = 5;

    private static final Logger logger = Logger.getLogger(ScanProcessor.class.getName());

    @Autowired
    public ScanProcessor() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1); // Set the desired number of threads for processing
        taskExecutor.setMaxPoolSize(poolSize); // Set the maximum number of threads for processing this is (N)
        taskExecutor.setThreadNamePrefix("ScanProcessor-");
        taskExecutor.initialize();
    }

    public void processScan(Long scanId) {
        try {
            taskExecutor.execute(() -> {
                // Fetch the scan from the database
                Optional<Scan> scanOptional = scanRepository.findById(scanId);
                logger.info("StartedProcessing scan with ID: " + scanId);
                if (scanOptional.isPresent()) {
                    Scan scan = scanOptional.get();
                    // Perform the necessary processing tasks for the scan

                    // Update the scan status (e.g., mark it as running or complete)
                    scan.setStatus(ScanStatus.RUNNING);
                    scanRepository.save(scan);
                    // Simulate the scan process
                    try {
                        Thread.sleep(5000); // Simulating the scan process
                        logger.info("Done Processing scan with ID: " + scanId);
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
            e.printStackTrace();
        }
    }
}
