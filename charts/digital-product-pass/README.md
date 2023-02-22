# Digital Product Passport Helm Chart

This helm release contains two helm deployments: digital product pass consumer frontend and backend with their default values.

## Prerequisites:

- Kubernetes 1.24+
- Helm 3.9.0+

## TL;DR
```bash
# Install a new helm release
helm install digital-product-passport ./charts/digital-product-passport --values=values.yaml

# Listing helm releases
helm list

# Uninstall helm chart
helm uninstall digital-product-passport
```
