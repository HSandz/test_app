# Prometheus & Grafana Monitoring Setup

Complete monitoring stack for Kubernetes with Prometheus, Grafana, and Alertmanager.

## 📊 Components

- **Prometheus** - Metrics collection and storage
- **Grafana** - Visualization and dashboards
- **Alertmanager** - Alert routing and notifications

## 🚀 Quick Start

### Prerequisites

1. **Start Docker Desktop** with Kubernetes enabled
2. Verify cluster is running:
   ```bash
   kubectl cluster-info
   ```

### Installation

Apply all monitoring manifests in order:

```bash
# Create namespace and configurations
kubectl apply -f monitoring/00-namespace.yaml
kubectl apply -f monitoring/01-prometheus-config.yaml
kubectl apply -f monitoring/02-alertmanager-config.yaml

# Create RBAC and deployments
kubectl apply -f monitoring/03-prometheus-rbac.yaml
kubectl apply -f monitoring/04-prometheus-deployment.yaml
kubectl apply -f monitoring/05-alertmanager-deployment.yaml
kubectl apply -f monitoring/06-grafana-deployment.yaml
```

Or apply all at once:
```bash
kubectl apply -f monitoring/
```

### Verify Installation

```bash
# Check all pods are running
kubectl get pods -n monitoring

# Expected output:
# NAME                            READY   STATUS    RESTARTS   AGE
# prometheus-xxx                  1/1     Running   0          1m
# alertmanager-xxx                1/1     Running   0          1m
# grafana-xxx                     1/1     Running   0          1m
```

## 🌐 Access the Dashboards

### Prometheus UI

```bash
kubectl port-forward -n monitoring svc/prometheus 9090:9090
```
- URL: http://localhost:9090
- Features: Query metrics, view alerts, check targets

### Grafana UI

```bash
kubectl port-forward -n monitoring svc/grafana 3000:3000
```
- URL: http://localhost:3000
- Username: `admin`
- Password: `admin123`

### Alertmanager UI

```bash
kubectl port-forward -n monitoring svc/alertmanager 9093:9093
```
- URL: http://localhost:9093
- Features: View active alerts, silences, configurations

## 📧 Configure Email Alerts

Edit `02-alertmanager-config.yaml`:

```yaml
global:
  smtp_smarthost: 'smtp.gmail.com:587'
  smtp_from: 'your-email@gmail.com'
  smtp_auth_username: 'your-email@gmail.com'
  smtp_auth_password: 'your-app-password'  # Gmail App Password
```

### Gmail App Password Setup

1. Go to Google Account → Security
2. Enable 2-Step Verification
3. Generate App Password: https://myaccount.google.com/apppasswords
4. Use the generated 16-character password in config

Update receiver email:
```yaml
receivers:
  - name: 'critical-alerts'
    email_configs:
      - to: 'your-email@example.com'  # Change this
```

Apply changes:
```bash
kubectl apply -f monitoring/02-alertmanager-config.yaml
kubectl rollout restart deployment/alertmanager -n monitoring
```

## 🔔 Configure Webhook Alerts

### Option 1: Local Webhook (Testing)

1. Install Node.js
2. Run the webhook receiver:
   ```bash
   cd monitoring
   node webhook-receiver.js
   ```
3. Configure port-forward for Alertmanager to reach localhost

### Option 2: External Webhook Service

Edit `02-alertmanager-config.yaml`:

```yaml
webhook_configs:
  - url: 'https://your-webhook-endpoint.com/alert'
    send_resolved: true
    http_config:
      basic_auth:
        username: 'your-username'
        password: 'your-password'
```

Popular webhook services:
- **Webhook.site** - https://webhook.site (testing)
- **RequestBin** - https://requestbin.com (testing)
- **Your own API** - Any HTTP endpoint that accepts POST requests

## 🎯 Configured Alerts

### Application Alerts

| Alert | Condition | Severity | Duration |
|-------|-----------|----------|----------|
| **PodDown** | Pod is not responding | Critical | 1 minute |
| **HighMemoryUsage** | Memory > 80% | Warning | 2 minutes |
| **HighCPUUsage** | CPU > 80% | Warning | 2 minutes |
| **PodFrequentlyRestarting** | Restart rate > 0 | Warning | 5 minutes |

### Kubernetes Alerts

| Alert | Condition | Severity | Duration |
|-------|-----------|----------|----------|
| **NodeNotReady** | Node status != Ready | Critical | 5 minutes |
| **LowDiskSpace** | Disk usage > 90% | Warning | 5 minutes |

## 📈 Grafana Dashboards

### Import Pre-built Dashboards

1. Open Grafana: http://localhost:3000
2. Go to **Dashboards** → **Import**
3. Import these popular dashboards by ID:

#### Recommended Dashboards:

- **3662** - Prometheus 2.0 Overview
- **1860** - Node Exporter Full
- **6417** - Kubernetes Cluster Monitoring
- **315** - Kubernetes Cluster Monitoring (Prometheus)
- **8588** - Kubernetes Deployment Statefulset Daemonset metrics

### Create Custom Dashboard

1. Click **+** → **Dashboard**
2. Add Panel
3. Select Prometheus data source
4. Use PromQL queries:

```promql
# CPU Usage
rate(container_cpu_usage_seconds_total{pod=~"course-management-app.*"}[5m]) * 100

# Memory Usage
container_memory_usage_bytes{pod=~"course-management-app.*"} / 1024 / 1024

# Pod Uptime
time() - kube_pod_start_time{pod=~"course-management-app.*"}

# HTTP Request Rate
rate(http_requests_total[5m])
```

## 🧪 Test Alerts

### Manually Trigger Alert

1. **Scale down your app** (triggers PodDown alert):
   ```bash
   kubectl scale deployment course-management-app -n assignment-5 --replicas=0
   ```

2. **Check alert in Prometheus**:
   - Go to http://localhost:9090/alerts
   - Wait 1-2 minutes for alert to fire

3. **Check Alertmanager**:
   - Go to http://localhost:9093
   - View active alerts

4. **Check your email/webhook**:
   - Should receive notification

5. **Restore app**:
   ```bash
   kubectl scale deployment course-management-app -n assignment-5 --replicas=1
   ```

### Generate CPU Load (Optional)

```bash
# Get pod name
kubectl get pods -n assignment-5

# Execute stress test
kubectl exec -n assignment-5 <pod-name> -- sh -c "yes > /dev/null &"

# Stop after testing
kubectl delete pod -n assignment-5 <pod-name>
```

## 🔍 Useful Commands

```bash
# View Prometheus logs
kubectl logs -n monitoring deployment/prometheus -f

# View Alertmanager logs
kubectl logs -n monitoring deployment/alertmanager -f

# View Grafana logs
kubectl logs -n monitoring deployment/grafana -f

# Check alert rules status
kubectl exec -n monitoring deployment/prometheus -- promtool check rules /etc/prometheus/rules/alert-rules.yml

# Reload Prometheus configuration
kubectl exec -n monitoring deployment/prometheus -- wget --post-data="" http://localhost:9090/-/reload

# List all services
kubectl get svc -n monitoring
```

## 🛠️ Troubleshooting

### Prometheus Not Scraping Targets

1. Check ServiceAccount permissions:
   ```bash
   kubectl get clusterrolebinding prometheus
   ```

2. View Prometheus logs:
   ```bash
   kubectl logs -n monitoring deployment/prometheus
   ```

3. Check targets in UI: http://localhost:9090/targets

### Alerts Not Firing

1. Check alert rules syntax:
   ```bash
   kubectl exec -n monitoring deployment/prometheus -- promtool check rules /etc/prometheus/alert-rules.yml
   ```

2. Verify Alertmanager connection:
   - http://localhost:9090/config (check alerting section)
   - http://localhost:9090/status (check alertmanagers)

### Email Alerts Not Working

1. Verify SMTP credentials in Alertmanager config
2. Check Alertmanager logs:
   ```bash
   kubectl logs -n monitoring deployment/alertmanager | grep -i smtp
   ```
3. Test with webhook first to isolate issue

### Grafana Can't Connect to Prometheus

1. Check datasource configuration:
   ```bash
   kubectl get cm grafana-datasources -n monitoring -o yaml
   ```

2. Verify Prometheus service:
   ```bash
   kubectl get svc prometheus -n monitoring
   ```

## 📚 Additional Resources

- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [Alertmanager Configuration](https://prometheus.io/docs/alerting/latest/configuration/)
- [PromQL Tutorial](https://prometheus.io/docs/prometheus/latest/querying/basics/)

## 🔄 Update Configuration

After modifying any ConfigMap:

```bash
# Apply changes
kubectl apply -f monitoring/XX-configmap-file.yaml

# Restart affected deployment
kubectl rollout restart deployment/<deployment-name> -n monitoring
```

## 🗑️ Uninstall

```bash
# Delete all monitoring resources
kubectl delete -f monitoring/

# Or delete namespace (removes everything)
kubectl delete namespace monitoring
```

## 📝 Next Steps

1. ✅ Configure email credentials
2. ✅ Set up webhook endpoint
3. ✅ Import Grafana dashboards
4. ✅ Test alerts by scaling down pods
5. ✅ Customize alert rules for your needs
6. ✅ Set up data retention policies
7. ✅ Add persistent volumes for production
