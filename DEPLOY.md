# 🚀 Deploy to Railway (Free)

## Step 1: Deploy Your App (2 minutes)

### Option A: One-Click Deploy
[![Deploy on Railway](https://railway.app/button.svg)](https://railway.app/template/spring-boot)

### Option B: Manual Deploy
```bash
# 1. Install Railway CLI
npm install -g @railway/cli

# 2. Login to Railway
railway login

# 3. Initialize project
railway init

# 4. Add environment variables
railway variables set GROQ_API_KEY=your_groq_key_here

# 5. Build locally first
mvn clean package -DskipTests

# 6. Deploy
railway up
```

## Step 2: Your App is Live! 🎉

After deployment, Railway will give you a URL like:
```
https://clinical-copilot-production.up.railway.app
```

## Step 3: Make it Installable (PWA)

Users can now install your app:

### On Mobile:
1. Visit your Railway URL on phone
2. Browser shows "Add to Home Screen"
3. Tap to install
4. App appears on home screen like native app

### On Desktop:
1. Visit your Railway URL in Chrome/Edge
2. Look for install icon in address bar
3. Click to install
4. App opens in its own window

## 🔧 Environment Variables

Set these in Railway dashboard:
```bash
GROQ_API_KEY=gsk_your_key_here
OPENROUTER_API_KEY=sk-or-v1_your_key_here  # Optional
```

## 📱 Share Your App

Send this link to users:
```
https://your-app-name.up.railway.app/mobile.html
```

They can:
- Use it in browser
- Install as PWA (recommended)
- Works offline after first visit

## 💰 Railway Free Tier

- **$5 free credit monthly**
- **500 hours execution time**
- **1GB RAM, 1 vCPU**
- **Perfect for MVP testing**

## 🚀 Alternative Free Platforms

If Railway credit runs out:

### Render (Free)
```bash
# Connect GitHub repo to Render
# Auto-deploys on git push
# 750 hours/month free
```

### Heroku (Free dyno hours)
```bash
heroku create clinical-copilot
git push heroku main
```

### Vercel (Frontend only)
```bash
# For static PWA version
vercel --prod
```

## 🎯 Next Steps

1. **Deploy now** - Get your app live in 2 minutes
2. **Test PWA install** - Try installing on your phone
3. **Share with users** - Send them the Railway URL
4. **Collect feedback** - Iterate based on real usage
5. **Scale up** - Move to paid tier when ready

**Your app will be live at a public URL that anyone can access and install!**