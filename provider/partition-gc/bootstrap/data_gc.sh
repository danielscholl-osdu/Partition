#!/usr/bin/env bash

# FIXME (GONRG-7695): Move elastic properties to additional partition when resolved
gc_system_partition_data() {
  DATA_PARTITION_ID_UPPER="${DATA_PARTITION_ID_VALUE^^}"
  cat <<EOF
{
  "properties": {
    "projectId": {
      "sensitive": false,
      "value": "${PROJECT_ID}"
    },
    "serviceAccount": {
      "sensitive": false,
      "value": "${SERVICE_ACCOUNT}"
    },
    "complianceRuleSet": {
      "sensitive": false,
      "value": "shared"
    },
    "dataPartitionId": {
      "sensitive": false,
      "value": "${DATA_PARTITION_ID_VALUE}"
    },
    "name": {
      "sensitive": false,
      "value": "${DATA_PARTITION_ID_VALUE}"
    },
    "bucket": {
      "sensitive": false,
      "value": "${PROJECT_ID}-${DATA_PARTITION_ID_VALUE}-records"
    },
    "seismicBucket": {
      "sensitive": false,
      "value": "${PROJECT_ID}-${DATA_PARTITION_ID_VALUE}-ss-seismic"
    },
    "crmAccountID": {
      "sensitive": false,
      "value": "[${DATA_PARTITION_ID_VALUE},${DATA_PARTITION_ID_VALUE}]"
    },
    "elasticsearch.host": {
      "sensitive": true,
      "value": "ELASTIC_HOST${PARTITION_SUFFIX}"
    },
    "elasticsearch.port": {
      "sensitive": true,
      "value": "ELASTIC_PORT${PARTITION_SUFFIX}"
    },
    "elasticsearch.user": {
      "sensitive": true,
      "value": "ELASTIC_USER${PARTITION_SUFFIX}"
    },
    "elasticsearch.password": {
      "sensitive": true,
      "value": "ELASTIC_PASS${PARTITION_SUFFIX}"
    },
    "entitlements.datasource.url": {
      "sensitive": true,
      "value": "ENT_PG_URL${PARTITION_SUFFIX}"
    },
    "entitlements.datasource.username": {
      "sensitive": true,
      "value": "ENT_PG_USER${PARTITION_SUFFIX}"
    },
    "entitlements.datasource.password": {
      "sensitive": true,
      "value": "ENT_PG_PASS${PARTITION_SUFFIX}"
    },
    "entitlements.datasource.schema": {
      "sensitive": true,
      "value": "ENT_PG_SCHEMA_${DATA_PARTITION_ID_UPPER}"
    },
    "reservoir-connection": {
      "sensitive": true,
      "value": "RESERVOIR_POSTGRES_CONN_STRING${PARTITION_SUFFIX}"
    },
    "wellbore-dms-bucket": {
      "sensitive": false,
      "value": "${PROJECT_ID}-${DATA_PARTITION_ID_VALUE}-logstore"
    }
  }
}
EOF
}

gc_additional_partition_data() {
  cat <<EOF
{
  "properties": {
    "policy-service-enabled": {
      "sensitive": false,
      "value": "false"
    },
    "kubernetes-secret-name": {
      "sensitive": false,
      "value": "eds-${DATA_PARTITION_ID_VALUE}"
    },
    "index-augmenter-enabled": {
      "sensitive": false,
      "value": "${INDEXER_AUGMENTER_ENABLED}"
    }
  }
}
EOF
}
