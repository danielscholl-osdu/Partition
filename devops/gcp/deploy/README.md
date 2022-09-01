<!--- Deploy -->

# Deploy helm chart

## Introduction

This chart bootstraps a deployment on a [Kubernetes](https://kubernetes.io) cluster using [Helm](https://helm.sh) package manager.

## Prerequisites

The code was tested on **Kubernetes cluster** (v1.21.11) with **Istio** (1.12.6)
  > Istio is installed with Istio Ingress Gateway

- Kubernetes cluster version can be checked with the command:

    `kubectl version --short | grep Server`

    The output will be similar to the following:

  ```console
  Server Version: v1.21.11-gke.1100
  ```

- Istio version can be checked in different ways, it is out of scope for this README. You can find more information [here](https://istio.io/latest/docs/setup/install/).

    The following command shows how to check version if Anthos Service Mesh is used:

    `kubectl -n istio-system get pods -lapp=istiod -o=jsonpath='{.items[0].metadata.labels.istio\.io/rev}'`

    The output will be similar to the following:

  ```console
  asm-1132-5
  ```

> It is possible to use other versions, but it hasn't been tested

This example describes installation in **Development mode**:

- In this mode helm chart is installed to the namespace **not labeled with Istio**.
  > More information about labeling can be found [here](https://istio.io/latest/docs/setup/additional-setup/sidecar-injection) (Istio) or [here](https://cloud.google.com/service-mesh/docs/managed/select-a-release-channel#default-injection-labels) (Anthos Service Mesh)

    You can find all labels for your namespace with the command:

     `kubectl get namespace <namespace> -o jsonpath={.metadata.labels}`

    The output shows that there are no any labels related to Istio:
  
    ```console
    {"kubernetes.io/metadata.name":"default"}
    ```

    When the namespace is labeled with Istio, the output could be:

    ```console
    {"istio-injection":"enabled","kubernetes.io/metadata.name":"default"}
    ```

### Operation system

The code works in Debian-based Linux (Debian 10 and Ubuntu 20.04) and Windows WSL 2. Also, it works but is not guaranteed in Google Cloud Shell. All other operating systems, including macOS, are not verified and supported.

### Packages

Packages are only needed for installation from a local computer.

- **HELM** (version: v3.7.1 or higher) [helm](https://helm.sh/docs/intro/install/)

    Helm version can be checked with the command:

    `helm version --short`

    The output will be similar to the following:

  ```console
  v3.7.1+gd141386
  ```

- **Kubectl** (version: v1.21.0 or higher) [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl)

    Kubectl version can be checked with the command:

    `kubectl version --short | grep Client`

    The output will be similar to the following:

  ```console
  Client Version: v1.21.0
  ```

## Installation

First you need to set variables in **values.yaml** file using any code editor. Some of the values are prefilled, but you need to specify some values as well. You can find more information about them below.

### Common variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**image** | your image name | string | - | yes
**requestsCpu** | amount of requests CPU | string | 0.1 | yes
**requestsMemory** | amount of requests memory| string | 260M | yes
**limitsCpu** | CPU limit | string | 1 | yes
**limitsMemory** | memory limit | string | 1G | yes
**serviceAccountName** | name of your service account | string | partition | yes
**imagePullPolicy** | when to pull image | string | IfNotPresent | yes

### Bootstrap variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**bootstrapImage** | name of the bootstrap image | string | - | yes
**bootstrapServiceAccountName** | name of the bootstrap SA | string | - | yes

### Config variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**domain** | your domain | string | - | yes
**appName** | name of the app | string | partition | yes
**cicdEnabled** | whether CI/CD is enabled | boolean | false | yes
**configmap** | configmap to be used | string | partition-config | yes
**onPremEnabled** | whether on-prem is enabled | boolean | false | yes
**publicAvailable** | public access to /api/partition | boolean | false | yes
**secret** | secret for postgres | string | partition-postgres-secret | yes

### Install the helm chart

Run this command from within this directory:

```console
helm install gcp-partition-deploy .
```

## Uninstalling the Chart

To uninstall the helm deployment:

```console
helm uninstall gcp-partition-deploy
```

To delete secrets and PVCs:

```console
kubectl delete secret --all; kubectl delete pvc --all
```

[Move-to-Top](#deploy-helm-chart)
