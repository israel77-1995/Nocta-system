package za.co.ccos.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CoordinationServiceTest {
    
    @Mock
    private LlamaAdapter llamaAdapter;
    
    private CoordinationService coordinationService;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        coordinationService = new CoordinationService(llamaAdapter, objectMapper);
    }
    
    @Test
    void testGenerateActions() throws Exception {
        String mockResponse = "{\"actions\":[{\"id\":\"a1\",\"type\":\"LAB_ORDER\",\"order\":{\"name\":\"CBC\",\"code\":\"CBC\"}}],\"notes\":\"Follow up in 1 week\"}";
        
        when(llamaAdapter.runPrompt(any(), any()))
                .thenReturn(new LlamaResponse(mockResponse));
        
        String structuredJson = "{\"chief_complaint\":\"headache\"}";
        String soapJson = "{\"assessment\":\"Migraine\"}";
        
        String actions = coordinationService.generateActions(structuredJson, soapJson);
        
        assertNotNull(actions);
        assertTrue(actions.contains("actions"));
    }
}
