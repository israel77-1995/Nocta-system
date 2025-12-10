// PWA Install Prompt
let deferredPrompt;
let installButton;

window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  deferredPrompt = e;
  showInstallButton();
});

function showInstallButton() {
  // Create install button if it doesn't exist
  if (!installButton) {
    installButton = document.createElement('button');
    installButton.className = 'install-btn';
    installButton.innerHTML = `
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
        <polyline points="7,10 12,15 17,10"/>
        <line x1="12" y1="15" x2="12" y2="3"/>
      </svg>
      Install App
    `;
    installButton.onclick = installApp;
    
    // Add to login screen
    const loginScreen = document.getElementById('loginScreen');
    if (loginScreen) {
      loginScreen.appendChild(installButton);
    }
  }
  installButton.style.display = 'block';
}

async function installApp() {
  if (deferredPrompt) {
    deferredPrompt.prompt();
    const { outcome } = await deferredPrompt.userChoice;
    
    if (outcome === 'accepted') {
      console.log('PWA installed');
      installButton.style.display = 'none';
    }
    deferredPrompt = null;
  }
}

// Show install banner for iOS Safari
function showIOSInstallBanner() {
  const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
  const isInStandaloneMode = window.navigator.standalone;
  
  if (isIOS && !isInStandaloneMode) {
    const banner = document.createElement('div');
    banner.className = 'ios-install-banner';
    banner.innerHTML = `
      <div class="banner-content">
        <span>📱 Install Clinical Copilot</span>
        <p>Tap <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg> then "Add to Home Screen"</p>
        <button onclick="this.parentElement.parentElement.remove()">×</button>
      </div>
    `;
    document.body.appendChild(banner);
  }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
  showIOSInstallBanner();
});