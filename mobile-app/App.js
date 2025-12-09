import React, { useRef, useState } from 'react';
import { StyleSheet, View, Text, ActivityIndicator, Platform, BackHandler } from 'react-native';
import { WebView } from 'react-native-webview';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaView } from 'react-native-safe-area-context';

const API_URL = 'http://192.168.8.114:8080';

export default function App() {
  const webViewRef = useRef(null);
  const [error, setError] = useState(null);

  React.useEffect(() => {
    if (Platform.OS === 'android') {
      const backHandler = BackHandler.addEventListener('hardwareBackPress', () => {
        if (webViewRef.current) {
          webViewRef.current.goBack();
          return true;
        }
        return false;
      });
      return () => backHandler.remove();
    }
  }, []);

  return (
    <SafeAreaView style={styles.container} edges={['top', 'bottom']}>
      <StatusBar style="dark" />
      {error && (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>Cannot connect to server</Text>
          <Text style={styles.errorSubtext}>{API_URL}</Text>
          <Text style={styles.errorHint}>Make sure backend is running</Text>
        </View>
      )}
      <WebView
        ref={webViewRef}
        source={{ uri: `${API_URL}/mobile.html?v=19` }}
        style={styles.webview}
        javaScriptEnabled={true}
        domStorageEnabled={true}
        startInLoadingState={true}
        scalesPageToFit={true}
        mediaPlaybackRequiresUserAction={false}
        allowsInlineMediaPlayback={true}
        mixedContentMode="compatibility"
        cacheEnabled={false}
        incognito={true}
        onLoadEnd={() => setError(null)}
        onError={(e) => setError(e.nativeEvent.description)}
        onMessage={(event) => console.log('WebView:', event.nativeEvent.data)}
        renderLoading={() => (
          <View style={styles.loadingContainer}>
            <ActivityIndicator size="large" color="#4F46E5" />
            <Text style={styles.loadingText}>Loading Clinical Copilot...</Text>
          </View>
        )}
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
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F9FAFB',
  },
  loadingText: {
    marginTop: 16,
    fontSize: 16,
    color: '#6B7280',
  },
  errorContainer: {
    padding: 20,
    backgroundColor: '#FEE2E2',
    borderBottomWidth: 1,
    borderBottomColor: '#EF4444',
  },
  errorText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#991B1B',
    marginBottom: 4,
  },
  errorSubtext: {
    fontSize: 12,
    color: '#991B1B',
    marginBottom: 4,
  },
  errorHint: {
    fontSize: 12,
    color: '#DC2626',
  },
});
