# GitHub Actions CI Setup

This project includes GitHub Actions workflows for Continuous Integration (CI).

## Available Workflows

### 1. **Complete CI Pipeline** (`ci.yml`)
Comprehensive CI pipeline with multiple checks:

- **Build Job**
  - Builds Maven project
  - Runs all unit tests
  - Generates test reports
  - Uploads test results as artifacts
  - Uploads JAR file as artifact

- **Code Quality Job**
  - Checkstyle (code style analysis)
  - SpotBugs (bug detection)

- **Docker Build Job**
  - Builds Docker image
  - Uses GitHub Actions cache for faster builds

- **Security Scan Job**
  - OWASP Dependency Check (vulnerability scanning)

**Trigger:** Push to `master`, `main`, or `develop` branches, or pull requests to `master`/`main`

### 2. **Simple CI Pipeline** (`ci-simple.yml`)
Lightweight CI pipeline focused on essentials:

- Builds Maven project
- Runs tests
- Uploads JAR artifact

**Use this if:** You want a faster, simpler CI without code quality and security checks

**Trigger:** Push to `master`, `main`, or `develop` branches, or pull requests to `master`/`main`

## Workflow Status

View the status of your CI workflows:
1. Go to your GitHub repository
2. Click **"Actions"** tab
3. Select a workflow to see details and logs

## Available Artifacts

After a successful build, the following artifacts are available for download:

- **jar-file**: The compiled Spring Boot JAR application
- **test-results**: JUnit test reports (from full CI pipeline)

Artifacts are automatically deleted after 7 days.

## Next Steps: Continuous Deployment (CD)

When ready to add CD, you can:

1. **Push to Docker Registry**
   ```yaml
   - name: Push Docker image
     uses: docker/build-push-action@v5
     with:
       context: .
       push: true
       tags: your-registry/assignment_5:latest
       registry: your-registry
   ```

2. **Deploy to Kubernetes**
   ```yaml
   - name: Deploy to K8s
     run: kubectl apply -f k8s-*.yaml
   ```

3. **Deploy to Cloud Platforms**
   - AWS ECS, ECR
   - Azure Container Registry (ACR), App Service
   - Google Cloud Run, GKE
   - Heroku
   - DigitalOcean

## Customization

### To enable/disable workflows:
- Rename `ci.yml` → `.ci.yml` to disable the complete pipeline
- Or delete the workflow file you don't want

### To change branch triggers:
Edit the `on:` section in the workflow file:
```yaml
on:
  push:
    branches: [ master, develop, staging ]  # Add/remove branches
  pull_request:
    branches: [ master ]
```

### To add more Java versions:
```yaml
strategy:
  matrix:
    java-version: ['21', '20', '17']
```

### To run on schedule (nightly builds):
```yaml
on:
  schedule:
    - cron: '0 2 * * *'  # Daily at 2 AM UTC
```

## Troubleshooting

### Workflow not triggering?
- Ensure the workflow file is in `.github/workflows/` directory
- Check branch name matches the `on:` trigger
- Push to the repository to trigger workflows

### Build failing?
- Click on the failed workflow
- Check the "Build" or "Run tests" step logs
- Common issues:
  - Java version mismatch
  - Missing dependencies
  - Test failures

### Cache not working?
- GitHub Actions cache is per-branch
- First build will be slower (cache building)
- Subsequent builds on same branch will be faster

## Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven in GitHub Actions](https://github.com/actions/setup-java)
- [Docker GitHub Actions](https://github.com/docker/build-push-action)
- [Upload Artifacts](https://github.com/actions/upload-artifact)

