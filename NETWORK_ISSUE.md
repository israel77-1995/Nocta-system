# Network Connectivity Issue 🔴

## Current Problem

Your application **cannot build** because your network is blocking Maven Central repository access.

### Test Results:
- ❌ Cannot connect to `repo.maven.apache.org:443`
- ❌ Cannot ping Google
- ❌ HTTPS (port 443) connections are being blocked

This is typically caused by:
1. **Corporate firewall** blocking Maven Central
2. **Proxy server** requiring authentication
3. **Network restrictions** in your organization
4. **VPN** interfering with connections

---

## ✅ What's Working

- ✓ Java 17 installed correctly
- ✓ API keys configured (Groq + OpenRouter)
- ✓ Source code ready
- ✓ Maven wrapper available

---

## 🔧 Solutions

### **Solution 1: Connect to Different Network (Fastest)**

If you're on a corporate network:
1. Connect to personal WiFi/mobile hotspot
2. Try building again:
   ```powershell
   $env:JAVA_HOME = "C:\Users\F8887986\AppData\Local\Programs\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
   .\mvnw.cmd clean package -DskipTests
   ```

---

### **Solution 2: Configure Proxy (If Using Corporate Network)**

If your organization uses a proxy:

1. **Find your proxy settings:**
   - Open Windows Settings → Network & Internet → Proxy
   - Note the proxy address and port

2. **Create Maven settings file:**
   Create file: `C:\Users\F8887986\.m2\settings.xml`

```xml
<settings>
  <proxies>
    <proxy>
      <id>corporate-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>your-proxy-host</host>
      <port>your-proxy-port</port>
      <username>your-username</username>
      <password>your-password</password>
      <nonProxyHosts>localhost|127.0.0.1</nonProxyHosts>
    </proxy>
  </proxies>
</settings>
```

3. **Retry build**

---

### **Solution 3: Use Pre-Built JAR (If Available)**

Check if someone on your team has a pre-built JAR file:

1. Get the `clinical-copilot-1.0.0.jar` file
2. Place it in `target/` folder
3. Run directly:
   ```powershell
   $env:GROQ_API_KEY = "gsk_your_key_here"
   $env:OPENROUTER_API_KEY = "sk-or-v1-your_key_here"
   java -jar target\clinical-copilot-1.0.0.jar
   ```

---

### **Solution 4: Contact IT Department**

Request access to:
- `repo.maven.apache.org` (port 443)
- `repo1.maven.org` (port 443)
- Maven Central repository whitelist

---

### **Solution 5: Build on Different Machine**

If you have access to another computer with internet:

1. Copy this entire folder to that machine
2. Build there:
   ```bash
   ./mvnw clean package -DskipTests
   ```
3. Copy the `target/clinical-copilot-1.0.0.jar` file back
4. Run on this machine

---

### **Solution 6: Use Docker (If Docker Desktop Available)**

If Docker Desktop works on your network:

```powershell
docker build -t clinical-copilot .
docker run -p 8080:8080 --env-file .env clinical-copilot
```

---

## 🔍 Diagnosis Commands

Check your network:

```powershell
# Test Maven Central
Test-NetConnection -ComputerName repo.maven.apache.org -Port 443

# Test general connectivity
ping google.com

# Check proxy settings
Get-ItemProperty -Path "HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings" | Select-Object ProxyServer, ProxyEnable

# Check firewall
Get-NetFirewallProfile | Select-Object Name, Enabled
```

---

## 📝 Summary

| Component | Status | Next Action |
|-----------|--------|-------------|
| Java 17 | ✅ Installed | None |
| API Keys | ✅ Configured | None |
| Network | ❌ Blocked | **FIX THIS** |
| Maven Build | ❌ Failed | Depends on network |

---

## 🎯 Recommended Action

**Option A: Use Mobile Hotspot**
1. Enable mobile hotspot on your phone
2. Connect your computer to hotspot
3. Run build command

**Option B: Ask IT for Help**
- Request Maven Central repository access
- Provide URLs: repo.maven.apache.org, repo1.maven.org

**Option C: Build Elsewhere**
- Use home computer/laptop with internet
- Copy JAR file back

---

## ⚡ Quick Test

To verify if network is the issue:

```powershell
# Test 1: Can you reach Maven Central?
Test-NetConnection repo.maven.apache.org -Port 443

# Test 2: Can you reach Google?
Test-NetConnection google.com -Port 443
```

If both fail → Network/Firewall issue  
If only Maven fails → Specific Maven Central block

---

## 📞 Need Help?

Contact your IT department with this info:

> "I need access to Maven Central repository to build a Java application.  
> Please whitelist: repo.maven.apache.org (port 443) and repo1.maven.org (port 443)"

---

## Alternative: Run Mobile App Only

The mobile app frontend doesn't need Maven, but it still needs the backend API running. Since we can't build the backend, the mobile app won't work standalone.

---

**Once network issue is resolved, building will take 3-5 minutes and the app will run successfully.**
