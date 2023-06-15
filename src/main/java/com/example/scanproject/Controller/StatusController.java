package com.example.scanproject.Controller;

import com.example.scanproject.Data.Scan;
import com.example.scanproject.Data.ScanRepository;
import com.example.scanproject.Data.ScanStatus;
import com.example.scanproject.Process.ScanProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private ScanRepository scanRepository;

    @GetMapping("/{scanId}")
    public ResponseEntity<String> getStatus(@PathVariable String scanId) {
        long ScanIdLong = 0L;
        try{
            ScanIdLong = Long.parseLong(scanId);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ScanStatus.NOT_FOUND.toString());

        }
        Optional<Scan> scanOptional = scanRepository.findById(ScanIdLong);
        if (scanOptional.isPresent()) {
            ScanStatus status = scanOptional.get().getStatus();
            return ResponseEntity.ok(status.toString());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}


