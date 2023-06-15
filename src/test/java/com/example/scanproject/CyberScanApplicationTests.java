package com.example.scanproject;

import com.example.scanproject.Controller.StatusController;
import com.example.scanproject.Controller.IngestController;
import com.example.scanproject.Data.Scan;
import com.example.scanproject.Data.ScanRepository;
import com.example.scanproject.Data.ScanStatus;

import com.example.scanproject.Process.ScanProcessor;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CyberScanApplicationTests {

    @MockBean
    private ScanRepository scanRepository;

    @InjectMocks
    private IngestController ingestController;

    @InjectMocks
    private StatusController statusController;

    @MockBean
    ScanProcessor scanProcessor;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingestController, statusController,scanRepository).build();
    }

    @Test
    public void testIngestController() throws Exception {
        // Mock the behavior of scanRepository.save() method
        when(scanRepository.save(any(Scan.class))).thenReturn(new Scan());

        MvcResult result = mockMvc.perform(post("/ingest"))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response);

        // Verify that the scan was saved
        verify(scanRepository).save(any(Scan.class));
    }

    @Test
    public void testStatusController() throws Exception {
        Long scanId = 1L;

        Scan scan = new Scan(scanId, ScanStatus.RUNNING, Instant.now());
        when(scanRepository.findById(scanId)).thenReturn(Optional.of(scan));

        mockMvc.perform(get("/status/{scanId}", scanId))
                .andExpect(status().isOk())
                .andExpect(content().string(ScanStatus.RUNNING.toString()));

        // Verify that the repository was queried with the correct scanId
        verify(scanRepository, times(1)).findById(scanId);
    }

    @Test
    public void testStatusControllerNotFound() throws Exception {
        Long scanId = 1L;

        when(scanRepository.findById(scanId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/status/{scanId}", scanId))
                .andExpect(status().isNotFound());

        // Verify that the repository was queried with the correct scanId
        verify(scanRepository, times(1)).findById(scanId);
    }
}
