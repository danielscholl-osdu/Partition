<!--- Configmap -->

# Configmap helm chart

## Introduction

This chart bootstraps a configmap deployment on a [Kubernetes](https://kubernetes.io) cluster using [Helm](https://helm.sh) package manager.

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
**logLevel** | logging level | string | INFO | yes
**springProfilesActive** | active spring profile | string | gcp | yes

### Google Cloud variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**projectId** | your Google Cloud project ID | string | -| yes
**partitionAdminAccounts** | admin account of partition | string | - | yes
**googleAudiences** | your Google Cloud client ID | string | - | yes
**serviceAccountTail** | ending of your service account | string | .iam.gserviceaccount.com | yes

> googleAudiences: If you are connected to Google Cloud console with `gcloud auth application-default login --no-browser` from your terminal, you can get your client_id using the command:

```console
cat ~/.config/gcloud/application_default_credentials.json | grep client_id
```

### Bootstrap common variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**partitionName** | name of the partition | string | partition | yes
**dataPartitionId** | ID of data partition | string | - | yes
**dataPartitionIdList** | List of data partitions - if not empty multipartition is enabled | array of strings | [] | no
**datafierSa** | datafier service account | string | datafier | yes

### Bootstrap on-prem variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**domain** | your domain | string | - | yes
**minioExternalEndpoint** | api url for external minio, if external minio is configured - this value will be set for MINIO_ENDPOINT and FILE_MINIO_ENDPOINT in bootstrap configmap| string | - | no

### Config variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**configmap** | configmap to be used | string | partition-config | yes
**appName** | name of the app | string | partition | yes
**onPremEnabled** | whether on-prem is enabled | boolean | false | yes

### Install the helm chart

Run this command from within this directory:

```console
helm install gcp-partition-configmap .
```

## Uninstalling the Chart

To uninstall the helm deployment:

```console
helm uninstall gcp-partition-configmap
```

To delete secrets and PVCs:

```console
kubectl delete secret --all; kubectl delete pvc --all
```

[Move-to-Top](#configmap-helm-chart)
