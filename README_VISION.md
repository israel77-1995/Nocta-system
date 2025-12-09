# 📸 Medical Image Analysis with AI Vision

## Current Status

✅ **Image Capture**: Working - doctors can take photos during consultations
⚠️ **AI Analysis**: Requires OpenRouter API key (optional)
✅ **Fallback Mode**: Provides structured documentation template

## How It Works Now

### Without OpenRouter Key (Current Setup)
1. Doctor clicks 📸 Photo button
2. Captures image of wound/rash/sore
3. System provides **structured documentation template**
4. Doctor fills in observations manually
5. Template added to consultation notes

### With OpenRouter Key (AI-Powered)
1. Doctor clicks 📸 Photo button
2. Captures image
3. **LLAMA 3.2 11B Vision AI analyzes image**
4. Provides instant clinical assessment:
   - Visual findings
   - Differential diagnosis (3-5 conditions)
   - Clinical significance
   - Recommended actions
   - Urgency level
5. AI analysis added to consultation notes

## Enable AI Vision (Optional)

### Step 1: Get Free OpenRouter API Key

1. Visit: **https://openrouter.ai/**
2. Click "Sign In" (top right)
3. Sign in with Google/GitHub (free, no credit card)
4. Go to: **https://openrouter.ai/keys**
5. Click "Create Key"
6. Copy the key (starts with `sk-or-v1-`)

### Step 2: Add to .env File

```bash
# Add this line to /home/wtc/Nocta-system/.env
OPENROUTER_API_KEY=sk-or-v1-your-key-here
```

### Step 3: Restart Backend

```bash
cd /home/wtc/Nocta-system
pkill -9 java
./start.sh
```

## Free Credits

- OpenRouter gives **$1 free credit** on signup
- Llama 3.2 11B Vision is **FREE** tier model
- No credit card required
- Enough for ~1000 image analyses

## Use Cases

✅ **Skin Conditions**: Rashes, lesions, dermatitis, eczema
✅ **Wounds**: Cuts, burns, ulcers, surgical sites
✅ **Infections**: Cellulitis, abscesses, fungal infections
✅ **Chronic Conditions**: Diabetic ulcers, pressure sores
✅ **Suspicious Lesions**: Moles, growths requiring assessment

## Current Behavior

**The app works perfectly without OpenRouter key!**

- Image capture: ✅ Working
- Documentation: ✅ Structured template provided
- Consultation flow: ✅ Complete
- Notes integration: ✅ Working

**Adding OpenRouter key enhances it with AI analysis but is NOT required.**
