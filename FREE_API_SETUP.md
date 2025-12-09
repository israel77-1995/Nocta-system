# LLAMA API Setup (100% LLAMA-Powered)

## Groq - Free Access to Meta's LLAMA Models

Groq provides **free API access to Meta's official LLAMA models**:
- LLAMA 3.3 70B (default - best quality)
- LLAMA 3.1 8B (faster)
- LLAMA 3.2 Vision (multimodal)

### 1. Get Free LLAMA API Key
1. Visit https://console.groq.com
2. Sign up (free account)
3. Go to API Keys section
4. Create new API key
5. Copy the key (starts with `gsk_`)

### 2. Configure Application

**Option A: Environment Variable (Recommended)**
```bash
export GROQ_API_KEY="gsk_your_actual_key_here"
```

**Option B: Edit application.yml**
```yaml
llama:
  provider: api
  groq:
    api-key: gsk_your_actual_key_here
```

### Available LLAMA Models

Edit `GroqLlamaAdapter.java` to change model:

```java
"model", "llama-3.3-70b-versatile",  // Default - best quality
// "model", "llama-3.1-8b-instant",   // Faster responses
// "model", "llama-3.2-11b-vision",   // For image analysis
```

### 3. Run Application
```bash
cd /home/wtc/Nocta-system
mvn clean package -DskipTests
java -jar target/clinical-copilot-1.0.0.jar
```

## Why Groq for LLAMA?

- **Official LLAMA Models**: Direct access to Meta's LLAMA 3.x models
- **Free Tier**: 14,400 requests/day (no credit card required)
- **Fast Inference**: Optimized hardware for LLAMA models
- **No Local Resources**: Runs in cloud, won't freeze your PC
- **Production Ready**: Same models used by Meta

## LLAMA Model Comparison

| Model | Size | Speed | Use Case |
|-------|------|-------|----------|
| LLAMA 3.3 70B | 70B params | Medium | Best quality (default) |
| LLAMA 3.1 8B | 8B params | Fast | Quick responses |
| LLAMA 3.2 Vision | 11B params | Medium | Image + text analysis |

## System Architecture

```
Clinical Copilot → Groq API → Meta LLAMA 3.3 70B → Response
```

**100% LLAMA-Powered:**
- Perception Agent: LLAMA 3.3 70B
- Documentation Agent: LLAMA 3.3 70B  
- Coordination Agent: LLAMA 3.3 70B
- Compliance Agent: LLAMA 3.3 70B

## Free Tier Limits

- **14,400 requests/day** (free forever)
- **No credit card required**
- **Production-grade LLAMA models**

## References

- LLAMA Cookbook: https://www.llama.com/resources/cookbook/
- Groq LLAMA API: https://console.groq.com
- Meta LLAMA: https://llama.meta.com
