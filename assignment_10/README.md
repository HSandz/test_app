# Data Lake Demo (Spring Boot + MinIO + DuckDB)

A basic web application demonstrating a Data Lake architecture using MinIO for object storage and DuckDB for querying CSV data.

## Prerequisites
- Java 21
- Kubernetes Cluster (Minikube, Docker Desktop, etc.)

## Setup & Run

1. **Deploy MinIO**:
   ```powershell
   kubectl apply -f k8s/minio.yaml
   ```

2. **Run Application**:
   Edit `run_app.ps1` to set your `JAVA_HOME` if needed, then run:
   ```powershell
   ./run_app.ps1
   ```

## API Usage

- **Upload CSV**: `POST /api/data/upload` (multipart/form-data, key: `file`)
- **Query Data**: `POST /api/data/query` (Body: SQL string, e.g., `SELECT * FROM read_csv_auto('s3://datalake/data.csv')`)
