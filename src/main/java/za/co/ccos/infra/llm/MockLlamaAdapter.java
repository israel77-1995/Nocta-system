package za.co.ccos.infra.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "llama.provider", havingValue = "mock", matchIfMissing = true)
@Slf4j
public class MockLlamaAdapter implements LlamaAdapter {
    
    @Override
    public LlamaResponse runPrompt(String prompt, LlamaOptions options) throws LlamaException {
        log.info("Mock LLAMA processing prompt (length: {})", prompt.length());
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        if (prompt.contains("SYSTEM: You are a clinical information extractor")) {
            return new LlamaResponse(generatePerceptionResponse(prompt), 250);
        } else if (prompt.contains("SYSTEM: You are a clinical document generator")) {
            return new LlamaResponse(generateDocumentationResponse(prompt), 450);
        } else if (prompt.contains("coordination")) {
            return new LlamaResponse(generateCoordinationResponse(prompt), 200);
        } else if (prompt.contains("compliance")) {
            return new LlamaResponse(generateComplianceResponse(prompt), 150);
        }
        
        return new LlamaResponse("{\"status\":\"processed\"}", 100);
    }
    
    private String generatePerceptionResponse(String prompt) {
        String transcript = extractBetween(prompt, "INPUT:", "CONTEXT:");
        if (transcript == null || transcript.isEmpty()) {
            transcript = prompt;
        }
        
        boolean hasHeadache = transcript.toLowerCase().contains("headache");
        boolean hasFever = transcript.toLowerCase().contains("fever");
        boolean hasCough = transcript.toLowerCase().contains("cough");
        boolean hasChestPain = transcript.toLowerCase().contains("chest") && transcript.toLowerCase().contains("pain");
        
        StringBuilder symptoms = new StringBuilder("[");
        if (hasHeadache) {
            symptoms.append("{\"name\":\"headache\",\"onset\":\"3 days\",\"severity\":\"moderate\",\"modifier\":\"throbbing, worse with light\"},");
        }
        if (hasFever) {
            symptoms.append("{\"name\":\"fever\",\"onset\":\"2 days\",\"severity\":\"moderate\",\"modifier\":\"intermittent\"},");
        }
        if (hasCough) {
            symptoms.append("{\"name\":\"cough\",\"onset\":\"5 days\",\"severity\":\"mild\",\"modifier\":\"dry, worse at night\"},");
        }
        if (hasChestPain) {
            symptoms.append("{\"name\":\"chest pain\",\"onset\":\"1 day\",\"severity\":\"severe\",\"modifier\":\"sharp, worse with breathing\"},");
        }
        if (symptoms.length() > 1) {
            symptoms.setLength(symptoms.length() - 1);
        }
        symptoms.append("]");
        
        String chiefComplaint = hasHeadache ? "headache" : hasFever ? "fever" : hasCough ? "cough" : hasChestPain ? "chest pain" : "general malaise";
        
        return String.format("""
            {
              "chief_complaint": "%s",
              "symptoms": %s,
              "duration": "3 days",
              "vitals": {"bp": "120/80", "hr": "78", "temp": "37.2"},
              "medications_reported": [],
              "possible_differentials": ["%s", "viral infection", "stress-related"],
              "red_flags": [],
              "missing_questions": ["Any recent travel?", "Family history?"]
            }
            """, chiefComplaint, symptoms, hasHeadache ? "migraine" : hasFever ? "influenza" : "upper respiratory infection");
    }
    
    private String generateDocumentationResponse(String prompt) {
        String json = extractBetween(prompt, "INPUT:", "<<TRANSCRIPT>>");
        
        @SuppressWarnings("unused")
        boolean hasHeadache = json.toLowerCase().contains("headache");
        @SuppressWarnings("unused")
        boolean hasFever = json.toLowerCase().contains("fever");
        boolean hasCough = json.toLowerCase().contains("cough");
        
        String subjective = hasHeadache ? 
            "Patient presents with a 3-day history of moderate throbbing headache, worse with light exposure. Denies visual changes or neck stiffness." :
            hasFever ?
            "Patient reports 2-day history of intermittent fever with associated malaise. No recent travel or sick contacts." :
            "Patient presents with 5-day history of dry cough, worse at night. No shortness of breath or chest pain.";
        
        String objective = "Vitals: BP 120/80, HR 78, Temp 37.2°C. General appearance: Alert and oriented. HEENT: Normocephalic, atraumatic. Cardiovascular: Regular rate and rhythm. Respiratory: Clear to auscultation bilaterally.";
        
        String assessment = hasHeadache ?
            "Tension-type headache, likely stress-related. No red flags for secondary causes." :
            hasFever ?
            "Viral syndrome, likely self-limiting. Monitor for progression." :
            "Upper respiratory tract infection, viral etiology most likely.";
        
        String plan = hasHeadache ?
            "1. Ibuprofen 400mg PO TID PRN for pain\\n2. Adequate hydration and rest\\n3. Follow up in 1 week if symptoms persist\\n4. Return immediately if severe symptoms develop" :
            hasFever ?
            "1. Supportive care with rest and hydration\\n2. Acetaminophen 500mg PO Q6H PRN for fever\\n3. Monitor temperature\\n4. Return if fever >39°C or symptoms worsen" :
            "1. Supportive care\\n2. Dextromethorphan for cough suppression\\n3. Increase fluid intake\\n4. Follow up if symptoms persist >7 days";
        
        String icd10 = hasHeadache ?
            "[{\"code\":\"R51.9\",\"desc\":\"Headache, unspecified\",\"confidence\":0.88,\"rationale\":\"Primary complaint with no specific migraine features\"}]" :
            hasFever ?
            "[{\"code\":\"R50.9\",\"desc\":\"Fever, unspecified\",\"confidence\":0.85,\"rationale\":\"Fever without identified source\"}]" :
            "[{\"code\":\"R05.9\",\"desc\":\"Cough, unspecified\",\"confidence\":0.90,\"rationale\":\"Persistent dry cough consistent with URTI\"}]";
        
        return String.format("""
            {
              "soap": {
                "subjective": "%s",
                "objective": "%s",
                "assessment": "%s",
                "plan": "%s"
              },
              "patient_summary": "Patient evaluated for %s. Clinical examination unremarkable. Recommended supportive care with close monitoring.",
              "icd10_suggestions": %s,
              "confidence": 0.87
            }
            """, subjective, objective, assessment, plan, 
            hasHeadache ? "headache" : hasFever ? "fever" : "cough", icd10);
    }
    
    private String generateCoordinationResponse(String prompt) {
        @SuppressWarnings("unused")
        boolean hasHeadache = prompt.toLowerCase().contains("headache");
        boolean hasFever = prompt.toLowerCase().contains("fever");
        
        String actions = hasHeadache ?
            "[{\"id\":\"a1\",\"type\":\"PRESCRIPTION\",\"drug\":{\"name\":\"Ibuprofen\",\"dose\":\"400mg\",\"freq\":\"TID PRN x 7 days\"}},{\"id\":\"a2\",\"type\":\"FOLLOW_UP\",\"ref\":{\"specialty\":\"Primary Care\",\"reason\":\"reassess headache\",\"urgency\":\"1 week\"}}]" :
            "[{\"id\":\"a1\",\"type\":\"PRESCRIPTION\",\"drug\":{\"name\":\"Acetaminophen\",\"dose\":\"500mg\",\"freq\":\"Q6H PRN x 5 days\"}},{\"id\":\"a2\",\"type\":\"LAB_ORDER\",\"order\":{\"name\":\"CBC\",\"code\":\"CBC\",\"priority\":\"if fever persists\",\"reason\":\"rule out infection\"}}]";
        
        return String.format("{\"actions\": %s, \"notes\": \"Patient counseled on warning signs\"}", actions);
    }
    
    private String generateComplianceResponse(String prompt) {
        return """
            {
              "allergy_conflicts": [],
              "drug_interactions": [],
              "completeness_score": 0.92,
              "missing_elements": [],
              "compliance_passed": true
            }
            """;
    }
    
    private String extractBetween(String text, String start, String end) {
        int startIdx = text.indexOf(start);
        int endIdx = text.indexOf(end);
        if (startIdx >= 0 && endIdx > startIdx) {
            return text.substring(startIdx + start.length(), endIdx).trim();
        }
        return text;
    }
}
