# Docker Deployment Guide

## Quick Start with Docker

### Prerequisites
- Docker installed ([get it here](https://www.docker.com/products/docker-desktop))
- Docker Compose installed (comes with Docker Desktop)
- ~2GB free disk space

---

## **Option 1: Local Docker Deployment (Easiest)**

### Step 1: Build the Docker Image
```bash
cd /home/wtc/Nocta-system
docker build -t clinical-copilot:latest .
```

### Step 2: Run with Docker Compose
```bash
cd docker
docker-compose up -d
```

### Step 3: Check if it's running
```bash
# Check running containers
docker ps

# View logs
docker-compose logs -f app

# Test the API
curl http://localhost:8080/api/v1/consultations/
```

### Stop the application
```bash
docker-compose down
```

---

## **Option 2: Push to Docker Hub (Free)**

### Step 1: Create Docker Hub Account
- Go to [hub.docker.com](https://hub.docker.com)
- Sign up for free account
- Note your username

### Step 2: Login to Docker Hub
```bash
docker login
# Enter your Docker Hub username and password
```

### Step 3: Tag and Push Image
```bash
# Replace 'your-username' with your Docker Hub username
docker tag clinical-copilot:latest your-username/clinical-copilot:latest
docker push your-username/clinical-copilot:latest
```

### Step 4: Pull on another machine
```bash
docker pull your-username/clinical-copilot:latest
docker run -p 8080:8080 your-username/clinical-copilot:latest
```

---

## **Option 3: Push to GitHub Container Registry (GHCR)**

### Step 1: Create GitHub Personal Access Token
- Go to GitHub Settings → Developer settings → Personal access tokens
- Create token with `write:packages` scope
- Copy the token

### Step 2: Login to GHCR
```bash
echo YOUR_GITHUB_TOKEN | docker login ghcr.io -u YOUR_GITHUB_USERNAME --password-stdin
```

### Step 3: Tag and Push
```bash
docker tag clinical-copilot:latest ghcr.io/your-username/clinical-copilot:latest
docker push ghcr.io/your-username/clinical-copilot:latest
```

### Step 4: Pull and Run
```bash
docker pull ghcr.io/your-username/clinical-copilot:latest
docker run -p 8080:8080 ghcr.io/your-username/clinical-copilot:latest
```

---

## **Helpful Docker Commands**

### View image size
```bash
docker images | grep clinical-copilot
```

### Remove image
```bash
docker rmi clinical-copilot:latest
```

### View container logs
```bash
docker logs -f clinical-copilot
```

### Execute command in container
```bash
docker exec -it clinical-copilot bash
```

### Clean up unused images/containers
```bash
docker system prune
```

---

## **Docker Compose with LLAMA Server**

The `docker-compose.yml` includes an optional LLAMA server. To enable it:

```bash
cd docker
docker-compose --profile llama up -d
```

This starts both the Clinical Copilot app and LLAMA server in separate containers.

---

## **Environment Variables**

You can customize the app by setting environment variables:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:mem:clinicaldb \
  -e LLAMA_SERVER_URL=http://llama-server:5000 \
  clinical-copilot:latest
```

---

## **Production Deployment Considerations**

For production, consider:
1. **Use PostgreSQL instead of H2** - Update `application.yml`
2. **Enable HTTPS** - Configure Spring Security
3. **Set resource limits** - CPU/memory constraints
4. **Use health checks** - Already configured in Dockerfile
5. **Implement logging** - Send logs to centralized system
6. **Set up monitoring** - Use Prometheus/Grafana

Example production docker-compose:
```yaml
services:
  app:
    image: your-registry/clinical-copilot:v1.0
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/clinicaldb
```

---

## **Troubleshooting**

### Port already in use
```bash
# Find process using port 8080
lsof -i :8080
# Kill it or use a different port
docker run -p 9080:8080 clinical-copilot:latest
```

### Image build fails
```bash
# Build with verbose output
docker build --no-cache -t clinical-copilot:latest .
```

### Container exits immediately
```bash
# Check logs for errors
docker logs clinical-copilot
```

---

## **Next Steps**

1. ✅ Docker image built locally
2. 🔄 Push to Docker Hub or GHCR (free)
3. ☁️ Deploy to Azure, AWS, or Heroku
4. 🔐 Add HTTPS and authentication
5. 📊 Set up monitoring and logging

Need help deploying to the cloud? See [DEPLOYMENT.md](DEPLOYMENT.md)
