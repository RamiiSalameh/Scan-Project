package com.example.scanproject.Data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scan {

    @jakarta.persistence.Id
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScanStatus status;

    @CreatedDate
    private Instant createdAt;

}
