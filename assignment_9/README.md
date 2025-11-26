# Dify ChatBot

A simple Spring Boot application that integrates with Dify API to provide a ChatBot interface.

## Prerequisites

-   Java 21
-   Maven (wrapper included)
-   Dify API Key

## Setup

1.  **Configure Environment Variables**:
    Copy `.env.example` to `.env` and update with your credentials:
    ```properties
    DIFY_API_KEY=your_actual_api_key
    DIFY_BASE_URL=https://api.dify.ai/v1
    ```

## Running the Application

Run the application using the Maven wrapper:

```powershell
.\mvnw spring-boot:run
```

Access the chat interface at: [http://localhost:8080](http://localhost:8080)
