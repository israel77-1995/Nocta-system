# Mobile App Configuration Guide

## Fixed Issues

### 1. SafeAreaView Deprecation ✅
**Issue**: WARN SafeAreaView has been deprecated
**Solution**: Updated `App.js` to import `SafeAreaView` from `react-native-safe-area-context` instead of `react-native`

**Before**:
```javascript
import { StyleSheet, SafeAreaView, Platform, BackHandler } from 'react-native';
```

**After**:
```javascript
import { StyleSheet, Platform, BackHandler } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
```

The `react-native-safe-area-context` package is already in `package.json` as a dependency.

---

### 2. Network Connection Error (-1004) ✅
**Issue**: "Could not connect to the server" when loading the app
**Root Cause**: Mobile apps and emulators cannot access `localhost` the same way web browsers do

**Solution**: Implemented platform-aware API URL selection

```javascript
const getAPIURL = () => {
  if (Platform.OS === 'android') {
    return 'http://10.0.2.2:8080'; // Android emulator bridge to host
  }
  // For iOS or physical devices, update this to your machine's IP
  return 'http://localhost:8080';
};
const API_URL = getAPIURL();
```

---

## Setup Instructions by Platform

### 📱 iOS Simulator
1. Ensure backend is running on `http://localhost:8080`
2. Run: `npm start --ios` from `mobile-app/` directory
3. App will connect to `http://localhost:8080/mobile.html`

### 🤖 Android Emulator
1. Ensure backend is running on `http://localhost:8080`
2. Run: `npm start --android` from `mobile-app/` directory
3. App will automatically connect to `http://10.0.2.2:8080/mobile.html`
   - (`10.0.2.2` is Android's bridge to the host machine)

### 📲 Physical Device (iOS/Android)
1. Find your machine's IP address:
   ```bash
   # macOS/Linux
   ifconfig | grep "inet " | grep -v 127.0.0.1
   
   # Windows
   ipconfig
   ```
   Look for something like `192.168.x.x` or `10.0.x.x`

2. Update `App.js` with your IP:
   ```javascript
   const getAPIURL = () => {
     if (Platform.OS === 'android') {
       return 'http://10.0.2.2:8080';
     }
     return 'http://192.168.x.x:8080'; // Replace with your IP
   };
   ```

3. Ensure your device is on the same WiFi network as your machine

4. Run: `npm start` and choose the appropriate option for your device

---

## Backend Configuration

The backend must be running on port 8080. Verify with:

```bash
# Terminal 1: Start backend
cd /home/wtc/Nocta-system
./mvnw spring-boot:run

# Terminal 2: Check health
curl http://localhost:8080/api/v1/health
```

Expected response:
```json
{"status":"UP"}
```

---

## Troubleshooting

### App Still Can't Connect
1. **Check backend is running**:
   ```bash
   lsof -i :8080
   ```
   Should see Java process

2. **For Android**: Verify `10.0.2.2:8080` works from emulator terminal:
   ```bash
   adb shell curl http://10.0.2.2:8080/api/v1/health
   ```

3. **For Physical Device**: Ensure both device and machine are on same WiFi:
   ```bash
   ping 192.168.x.x  # Ping your machine's IP from device
   ```

4. **Check Expo tunnel**: If using `expo start --tunnel`, it creates a public URL:
   ```bash
   # This will show a QR code and URL
   npm start
   ```

### SafeAreaView Still Shows Warning
- Ensure `npm install` was run in `mobile-app/` directory
- Verify `react-native-safe-area-context` is in `node_modules`:
  ```bash
  ls -la mobile-app/node_modules/react-native-safe-area-context
  ```
- If missing, install: `cd mobile-app && npm install react-native-safe-area-context`

---

## Environment Variables (Optional)

For more flexibility, create a `.env.local` file in `mobile-app/`:

```bash
EXPO_PUBLIC_API_URL=http://192.168.x.x:8080
```

Then update `App.js`:
```javascript
const API_URL = process.env.EXPO_PUBLIC_API_URL || getAPIURL();
```

---

## Development vs Production

| Environment | Backend URL | Use Case |
|-------------|------------|----------|
| **Local Development** | `http://localhost:8080` | iOS Simulator on macOS |
| **Android Dev** | `http://10.0.2.2:8080` | Android Emulator on Windows/macOS/Linux |
| **Local Network** | `http://192.168.x.x:8080` | Physical device testing |
| **Cloud** | `https://api.yourdomain.com` | Production deployment |

---

## Next Steps

1. ✅ SafeAreaView import fixed
2. ✅ Platform-aware API URL configuration added
3. Test on your target platform using the setup instructions above
4. Once working, commit changes: `git add -A && git commit -m "Fix mobile app deprecation and network connectivity"`
