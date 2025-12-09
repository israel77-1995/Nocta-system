package za.co.ccos.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import za.co.ccos.domain.Patient;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaResponse;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PerceptionServiceTest {
    
    @Mock
    private LlamaAdapter llamaAdapter;
    
    private PerceptionService perceptionService;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        perceptionService = new PerceptionService(llamaAdapter, objectMapper);
    }
    
    @Test
    void testExtractStructuredFacts() throws Exception {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDob(LocalDate.of(1980, 5, 15));
        
        String mockResponse = "{\"chief_complaint\":\"headache\",\"symptoms\":[{\"name\":\"headache\",\"onset\":\"3 days\",\"severity\":\"severe\"}],\"duration\":\"3 days\",\"vitals\":null,\"medications_reported\":[],\"possible_differentials\":[\"migraine\"],\"red_flags\":[],\"missing_questions\":[]}";
        
        when(llamaAdapter.runPrompt(any(), any()))
                .thenReturn(new LlamaResponse(mockResponse));
        
        String transcript = "Patient reports severe headache for 3 days";
        JsonNode result = perceptionService.extractStructuredFacts(transcript, patient);
        
        assertNotNull(result);
        assertTrue(result.has("chief_complaint"));
        assertEquals("headache", result.get("chief_complaint").asText());
    }
}
