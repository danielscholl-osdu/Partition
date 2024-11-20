#!/usr/bin/env bash

gc_system_partition_data() {
  DATA_PARTITION_ID_UPPER=$(echo "${DATA_PARTITION_ID_VALUE//-/_}" | tr '[:lower:]' '[:upper:]')
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
    "crmAccountID": {
      "sensitive": false,
      "value": "[${DATA_PARTITION_ID_VALUE},${DATA_PARTITION_ID_VALUE}]"
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
    "system.schema.bucket.name": {
      "sensitive": false,
      "value": "${PROJECT_ID}-system-schema"
    },
    "system.featureFlag.eds.enabled": {
      "sensitive": false,
      "value": "${EDS_ENABLED}"
    },
    "system.featureFlag.opa.enabled": {
      "sensitive": false,
      "value": "${POLICY_SERVICE_ENABLED}"
    },
    "system.featureFlag.policy.enabled": {
      "sensitive": false,
      "value": "${POLICY_SERVICE_ENABLED}"
    },
    "system.featureFlag.autocomplete.enabled": {
      "sensitive": false,
      "value": "${AUTOCOMPLETE_ENABLED}"
    }
  }
}
EOF
}
