# Clinical Copilot - Mobile App Deployment Guide

## Overview

The mobile app is a **React Native wrapper** that packages your web UI into a downloadable native application for Android and iOS.

## Architecture

```
┌─────────────────────────────────┐
│   Native Mobile App (APK/IPA)  │
│   - React Native Container      │
│   - WebView Component           │
│   - Native Permissions          │
└────────────┬────────────────────┘
             │ HTTP/REST
┌────────────▼────────────────────┐
│   Spring Boot Backend           │
│   - REST API                    │
│   - SOAP Generation             │
│   - LLAMA Integration           │
└─────────────────────────────────┘
```

## Quick Demo Setup (5 minutes)

### Option 1: Test with Expo Go (Fastest)

1. **Install dependencies**:
```bash
cd mobile-app
npm install
```

2. **Start backend**:
```bash
cd ..
./mvnw spring-boot:run
```

3. **Get your computer's IP**:
```bash
# Linux/Mac
ifconfig | grep "inet " | grep -v 127.0.0.1

# Windows
ipconfig | findstr IPv4
```

4. **Update API URL** in `mobile-app/App.js`:
```javascript
const API_URL = 'http://YOUR_IP:8080'; // e.g., http://192.168.1.100:8080
```

5. **Start mobile app**:
```bash
npm start
```

6. **Install Expo Go** on your phone:
   - Android: https://play.google.com/store/apps/details?id=host.exp.exponent
   - iOS: https://apps.apple.com/app/expo-go/id982107779

7. **Scan QR code** from terminal with Expo Go app

✅ **Done!** App is running on your phone.

### Option 2: Build Downloadable APK (15 minutes)

1. **Create Expo account**: https://expo.dev/signup

2. **Install EAS CLI**:
```bash
npm install -g eas-cli
```

3. **Login**:
```bash
cd mobile-app
eas login
```

4. **Configure build**:
```bash
eas build:configure
```

5. **Build Android APK**:
```bash
eas build --platform android --profile preview
```

6. **Wait 5-10 minutes** for build to complete

7. **Download APK** from the provided link

8. **Install on Android device**:
   - Transfer APK to phone
   - Enable "Install from Unknown Sources"
   - Tap APK to install

✅ **Done!** Standalone app installed.

## Production Deployment

### Android (Google Play Store)

1. **Build production APK**:
```bash
eas build --platform android --profile production
```

2. **Submit to Play Store**:
```bash
eas submit --platform android
```

3. **Requirements**:
   - Google Play Developer account ($25 one-time)
   - App signing key
   - Store listing (screenshots, description)
   - Privacy policy URL

### iOS (Apple App Store)

1. **Build production IPA**:
```bash
eas build --platform ios --profile production
```

2. **Submit to App Store**:
```bash
eas submit --platform ios
```

3. **Requirements**:
   - Apple Developer account ($99/year)
   - App Store Connect access
   - Store listing
   - Privacy policy URL

## Configuration

### Backend URL

Edit `mobile-app/App.js`:

```javascript
// Development (local testing)
const API_URL = 'http://192.168.1.100:8080';

// Production (deployed backend)
const API_URL = 'https://api.clinicalcopilot.com';
```

### App Branding

Edit `mobile-app/app.json`:

```json
{
  "expo": {
    "name": "Clinical Copilot",
    "slug": "clinical-copilot-mobile",
    "version": "1.0.0",
    "ios": {
      "bundleIdentifier": "za.co.ccos.clinicalcopilot"
    },
    "android": {
      "package": "za.co.ccos.clinicalcopilot"
    }
  }
}
```

### App Icons

1. Create 1024x1024 icon with your logo
2. Generate all sizes: https://www.appicon.co/
3. Place in `mobile-app/assets/`
4. Rebuild app

## Features

✅ **Native App Experience**
- Installable from APK/IPA
- App icon on home screen
- Splash screen
- Native permissions

✅ **Web UI Integration**
- Wraps existing mobile.html
- No code duplication
- Update UI without rebuilding app

✅ **Native Capabilities**
- Microphone access for voice recording
- Camera access (future)
- Push notifications (future)
- Offline support (future)

✅ **Cross-Platform**
- Single codebase
- Android + iOS support
- Consistent experience

## Advantages Over Pure Web App

| Feature | Web App | Native App |
|---------|---------|------------|
| Installation | Bookmark only | Real app icon |
| App Stores | ❌ | ✅ |
| Offline | Limited | Full support |
| Permissions | Browser-dependent | Native prompts |
| Performance | Good | Better |
| Updates | Instant | OTA or rebuild |

## Cost Comparison

### Free Option (Expo Go)
- ✅ Unlimited testing
- ✅ Share with team
- ❌ Requires Expo Go app
- ❌ Not for end users

### Paid Options
- **EAS Build**: Free tier (30 builds/month)
- **Google Play**: $25 one-time
- **Apple App Store**: $99/year

## Troubleshooting

### "Cannot connect to server"

**Solution**: Update API_URL to your computer's IP address
```bash
# Find your IP
ifconfig | grep "inet " | grep -v 127.0.0.1

# Update App.js
const API_URL = 'http://YOUR_IP:8080';
```

### "Microphone not working"

**Solution**: Check permissions in `app.json`:
```json
{
  "android": {
    "permissions": ["RECORD_AUDIO"]
  },
  "ios": {
    "infoPlist": {
      "NSMicrophoneUsageDescription": "Record consultations"
    }
  }
}
```

### "Build failed on EAS"

**Solution**: 
1. Run `eas build:configure` again
2. Ensure unique bundle identifiers
3. Check Expo account is active

### "WebView shows blank screen"

**Solution**:
1. Test backend URL in phone browser first
2. Ensure backend is accessible on network
3. Check firewall allows port 8080

## Demo Checklist

- [ ] Backend running on accessible IP
- [ ] API_URL updated in App.js
- [ ] npm install completed
- [ ] Expo Go installed on phone
- [ ] Phone on same WiFi network
- [ ] npm start running
- [ ] QR code scanned
- [ ] App loads successfully
- [ ] Voice recording works
- [ ] SOAP notes generate

## Next Steps

1. **Test locally** with Expo Go
2. **Build APK** for stakeholder demos
3. **Gather feedback** from clinicians
4. **Add app icons** and branding
5. **Deploy backend** to production server
6. **Submit to stores** when ready

## Support

- **Expo Docs**: https://docs.expo.dev/
- **React Native**: https://reactnative.dev/
- **EAS Build**: https://docs.expo.dev/build/introduction/
