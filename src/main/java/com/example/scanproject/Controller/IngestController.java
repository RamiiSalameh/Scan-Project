package com.example.scanproject.Controller;

import com.example.scanproject.Data.Scan;
import com.example.scanproject.Data.ScanRepository;
import com.example.scanproject.Data.ScanStatus;
import com.example.scanproject.Process.ScanProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/ingest")
public class IngestController {
    private static final Logger logger = Logger.getLogger(ScanProcessor.class.getName());

    @Autowired
    private ScanProcessor scanProcessor;

    @Autowired
    private ScanRepository scanRepository;

    @PostMapping
    public ResponseEntity<?> ingest() {
        long scanId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE; // Generate a random positive long value
        Scan scan = new Scan(scanId, ScanStatus.ACCEPTED, Instant.now());
        scanRepository.save(scan);
        Optional<Scan> scan2 = scanRepository.findById(scanId);
        logger.info("This was saved:" + scan2.isPresent());
        // Invoke the processScan() method of the ScanProcessor directly
        scanProcessor.processScan(scanId);

        return ResponseEntity.status(HttpStatus.CREATED).body(scanId);
    }
}

