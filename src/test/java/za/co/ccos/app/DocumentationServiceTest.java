package za.co.ccos.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import za.co.ccos.domain.GeneratedNote;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DocumentationServiceTest {
    
    @Mock
    private LlamaAdapter llamaAdapter;
    
    private DocumentationService documentationService;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        documentationService = new DocumentationService(llamaAdapter, objectMapper);
    }
    
    @Test
    void testGenerateDocumentation() throws Exception {
        String mockResponse = "{\"soap\":{\"subjective\":\"Patient reports headache\",\"objective\":\"BP 140/90\",\"assessment\":\"Migraine\",\"plan\":\"Rest and hydration\"},\"patient_summary\":\"Patient with migraine\",\"icd10_suggestions\":[{\"code\":\"G43.0\",\"desc\":\"Migraine\",\"confidence\":0.9}],\"confidence\":0.85}";
        
        when(llamaAdapter.runPrompt(any(), any()))
                .thenReturn(new LlamaResponse(mockResponse));
        
        String structuredJson = "{\"chief_complaint\":\"headache\"}";
        String transcript = "Patient reports headache";
        
        GeneratedNote note = documentationService.generateDocumentation(
                structuredJson, transcript, UUID.randomUUID(), UUID.randomUUID());
        
        assertNotNull(note);
        assertNotNull(note.getSoapAssessment());
        assertTrue(note.getSoapAssessment().contains("Migraine"));
        assertNotNull(note.getIcd10Codes());
    }
}
