# Clinical Copilot Mobile App

Native mobile application wrapper for Clinical Copilot using React Native + Expo.

## Prerequisites

1. **Node.js 18+** and **npm**
2. **Expo CLI**: `npm install -g expo-cli`
3. **EAS CLI** (for building): `npm install -g eas-cli`
4. **Expo Go app** on your phone (for testing)

## Quick Start

### 1. Install Dependencies

```bash
cd mobile-app
npm install
```

### 2. Configure Backend URL

Edit `App.js` and set the correct API_URL:

```javascript
// For Android Emulator
const API_URL = 'http://10.0.2.2:8080';

// For iOS Simulator
const API_URL = 'http://localhost:8080';

// For Physical Device (replace with your computer's IP)
const API_URL = 'http://192.168.1.100:8080';
```

### 3. Run Development Server

```bash
npm start
```

This opens Expo DevTools. Scan the QR code with:
- **Android**: Expo Go app
- **iOS**: Camera app (opens in Expo Go)

## Building Downloadable Apps

### Android APK

1. **Create Expo account**: https://expo.dev/signup

2. **Login to EAS**:
```bash
eas login
```

3. **Configure project**:
```bash
eas build:configure
```

4. **Build APK**:
```bash
eas build --platform android --profile preview
```

5. **Download APK** from the link provided and install on Android device

### iOS IPA (Requires Apple Developer Account)

```bash
eas build --platform ios --profile preview
```

## Testing on Physical Devices

### Android
1. Enable USB debugging on your device
2. Connect via USB
3. Run: `npm run android`

### iOS
1. Connect iPhone via USB
2. Run: `npm run ios`
3. Trust developer certificate on device

## App Features

- **Native Container**: Wraps web UI in native WebView
- **Hardware Back Button**: Android back button navigation
- **Microphone Access**: Native permissions for voice recording
- **Offline Ready**: Can be installed without app store
- **Auto-updates**: Update web UI without rebuilding app

## Architecture

```
Mobile App (React Native)
    ↓ WebView
Web UI (mobile.html)
    ↓ HTTP/REST
Spring Boot Backend
    ↓
LLAMA AI
```

## Customization

### App Icon & Splash Screen

Replace these files in `assets/`:
- `icon.png` (1024x1024)
- `adaptive-icon.png` (1024x1024, Android)
- `splash.png` (1242x2436)
- `favicon.png` (48x48)

Generate assets: https://www.appicon.co/

### App Name & Bundle ID

Edit `app.json`:
```json
{
  "expo": {
    "name": "Your App Name",
    "ios": {
      "bundleIdentifier": "com.yourcompany.app"
    },
    "android": {
      "package": "com.yourcompany.app"
    }
  }
}
```

## Distribution

### Android
- **Direct Install**: Share APK file
- **Google Play**: Use `eas submit` after building production APK

### iOS
- **TestFlight**: Use `eas submit` for beta testing
- **App Store**: Submit production build

## Troubleshooting

**Cannot connect to backend:**
- Ensure Spring Boot is running
- Check API_URL matches your network setup
- For physical devices, use computer's local IP (not localhost)
- Disable firewall on port 8080

**Build fails:**
- Run `eas build:configure` again
- Check Expo account is set up
- Verify app.json has unique bundle identifiers

**Microphone not working:**
- Check permissions in app.json
- Grant microphone permission when app prompts
- For iOS, check Info.plist descriptions

**WebView blank:**
- Check backend is accessible from device
- Test URL in device browser first
- Check CORS settings in Spring Boot

## Development Tips

- Use Expo Go for rapid testing
- Build APK for stakeholder demos
- Update web UI without rebuilding app
- Use EAS Update for OTA updates

## Production Checklist

- [ ] Update API_URL to production server
- [ ] Add HTTPS support
- [ ] Configure proper app icons
- [ ] Set unique bundle identifiers
- [ ] Test on multiple devices
- [ ] Enable error tracking (Sentry)
- [ ] Configure app signing
- [ ] Prepare store listings

## Support

For issues with:
- **Expo/React Native**: https://docs.expo.dev/
- **Backend API**: See main project README
- **Build process**: https://docs.expo.dev/build/introduction/
