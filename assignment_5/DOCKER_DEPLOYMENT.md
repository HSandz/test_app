# Docker Deployment Guide

This guide explains how to deploy the Spring Boot Course Management application using Docker.

## Prerequisites

- Docker installed on your system ([Download Docker](https://www.docker.com/get-started))
- Docker Compose (usually comes with Docker Desktop)

## Deployment Options

### Option 1: Using Docker Compose (Recommended)

This is the easiest method for deployment.

#### 1. Build and Start the Application

```bash
docker-compose up -d --build
```

This command will:
- Build the Docker image
- Create and start the container
- Run it in detached mode (background)

#### 2. View Application Logs

```bash
docker-compose logs -f
```

#### 3. Stop the Application

```bash
docker-compose down
```

#### 4. Stop and Remove Data

```bash
docker-compose down -v
```

### Option 2: Using Docker Commands Directly

#### 1. Build the Docker Image

```bash
docker build -t spring-course-app .
```

#### 2. Run the Container

```bash
docker run -d \
  --name spring-course-app \
  -p 8080:8080 \
  -v ${PWD}/data:/app/data \
  spring-course-app
```

For PowerShell on Windows:
```powershell
docker run -d `
  --name spring-course-app `
  -p 8080:8080 `
  -v "${PWD}/data:/app/data" `
  spring-course-app
```

#### 3. View Logs

```bash
docker logs -f spring-course-app
```

#### 4. Stop the Container

```bash
docker stop spring-course-app
```

#### 5. Remove the Container

```bash
docker rm spring-course-app
```

## Accessing the Application

Once deployed, access the application at:

- **Main Application**: http://localhost:8080
- **H2 Console** (if enabled): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:/app/data/coursedb`
  - Username: `sa`
  - Password: `password`

## Default Credentials

After initial deployment, you can log in with:
- **Admin User**: 
  - Username: `admin`
  - Password: `admin123`
  
- **Regular User**: 
  - Username: `user`
  - Password: `user123`

## Data Persistence

The H2 database data is stored in the `./data` directory on your host machine. This ensures your data persists even when containers are recreated.

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, you can change it in `docker-compose.yml`:

```yaml
ports:
  - "9090:8080"  # Use port 9090 on host instead
```

### View Container Status

```bash
docker ps
```

### Access Container Shell

```bash
docker exec -it spring-course-app sh
```

### Rebuild Without Cache

```bash
docker-compose build --no-cache
docker-compose up -d
```

### Check Container Resource Usage

```bash
docker stats spring-course-app
```

## Production Considerations

For production deployment, consider:

1. **Use a Production Database**: Replace H2 with PostgreSQL or MySQL
2. **Environment Variables**: Store sensitive data in environment variables
3. **Reverse Proxy**: Use Nginx or Traefik for SSL/TLS
4. **Resource Limits**: Set memory and CPU limits
5. **Health Checks**: Add health check endpoints
6. **Monitoring**: Implement logging and monitoring solutions

### Example Production docker-compose.yml with PostgreSQL

```yaml
version: '3.8'

services:
  db:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: coursedb
      POSTGRES_USER: dbuser
      POSTGRES_PASSWORD: dbpassword
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - app-network

  web:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/coursedb
      - SPRING_DATASOURCE_USERNAME=dbuser
      - SPRING_DATASOURCE_PASSWORD=dbpassword
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    depends_on:
      - db
    networks:
      - app-network

volumes:
  postgres-data:

networks:
  app-network:
    driver: bridge
```

## Updating the Application

1. Stop the current container:
   ```bash
   docker-compose down
   ```

2. Pull latest code changes

3. Rebuild and restart:
   ```bash
   docker-compose up -d --build
   ```

## Cleanup

Remove all containers, images, and volumes:

```bash
docker-compose down -v
docker rmi spring-course-app
```
