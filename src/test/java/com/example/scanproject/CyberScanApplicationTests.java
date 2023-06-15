package com.example.scanproject;

import com.example.scanproject.Controller.IngestController;
import com.example.scanproject.Controller.StatusController;
import com.example.scanproject.Data.Scan;
import com.example.scanproject.Data.ScanRepository;
import com.example.scanproject.Data.ScanStatus;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class CyberScanApplicationTests {

    @Mock
    private ScanRepository scanRepository;

    @InjectMocks
    private IngestController ingestController;

    @InjectMocks
    private StatusController statusController;

    private MockMvc mockMvc;

    @Before("")
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingestController, statusController).build();
    }

    @Test
    public void testIngestController() throws Exception {
        MvcResult result = mockMvc.perform(post("/ingest"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response);

        // Verify that the scan was saved
        verify(scanRepository, times(1)).save(any(Scan.class));
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
