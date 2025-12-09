import React, { useRef } from 'react';
import { StyleSheet, Platform, BackHandler } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { WebView } from 'react-native-webview';
import { StatusBar } from 'expo-status-bar';

// Configure API URL based on platform
// For Android emulator: http://10.0.2.2:8080
// For iOS simulator: http://localhost:8080
// For physical device: Use your machine's IP address (e.g., http://192.168.x.x:8080)
const getAPIURL = () => {
  if (Platform.OS === 'android') {
    return 'http://10.0.2.2:8080'; // Android emulator bridge
  }
  // For iOS or physical devices, update this to your machine's IP
  return 'http://localhost:8080';
};
const API_URL = getAPIURL();

export default function App() {
  const webViewRef = useRef(null);

  React.useEffect(() => {
    const backHandler = BackHandler.addEventListener('hardwareBackPress', () => {
      if (webViewRef.current) {
        webViewRef.current.goBack();
        return true;
      }
      return false;
    });

    return () => backHandler.remove();
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="dark" />
      <WebView
        ref={webViewRef}
        source={{ uri: `${API_URL}/mobile.html` }}
        style={styles.webview}
        javaScriptEnabled={true}
        domStorageEnabled={true}
        startInLoadingState={true}
        scalesPageToFit={true}
        mediaPlaybackRequiresUserAction={false}
        allowsInlineMediaPlayback={true}
        mixedContentMode="compatibility"
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F9FAFB',
  },
  webview: {
    flex: 1,
  },
});
