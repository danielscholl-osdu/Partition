/*
  Copyright 2002-2021 Google LLC
  Copyright 2002-2021 EPAM Systems, Inc

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.opengroup.osdu.partition.provider.gcp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.opengroup.osdu.core.common.model.http.AppException;
import org.opengroup.osdu.core.common.provider.interfaces.IKmsClient;
import org.opengroup.osdu.partition.logging.AuditLogger;
import org.opengroup.osdu.partition.model.PartitionInfo;
import org.opengroup.osdu.partition.model.Property;
import org.opengroup.osdu.partition.provider.gcp.model.PartitionPropertyEntity;
import org.opengroup.osdu.partition.provider.gcp.repository.PartitionPropertyEntityRepository;
import org.opengroup.osdu.partition.provider.interfaces.IPartitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartitionServiceImpl implements IPartitionService {

  private static final String UNKNOWN_ERROR_REASON = "unknown error";


  private final PartitionPropertyEntityRepository partitionPropertyEntityRepository;

  private final IKmsClient kmsClient;

  private final AuditLogger auditLogger;

  @Override
  public PartitionInfo createPartition(String partitionId, PartitionInfo partitionInfo) {
    if (this.partitionPropertyEntityRepository.findByPartitionId(partitionId).isPresent()) {
      this.auditLogger.createdPartitionFailure(Collections.singletonList(partitionId));
      throw new AppException(HttpStatus.SC_CONFLICT, UNKNOWN_ERROR_REASON,
          "Partition already exists.");
    }
    List<PartitionPropertyEntity> partitionProperties = new ArrayList<>();
    for (Map.Entry<String, Property> entry : partitionInfo.getProperties().entrySet()) {
      PartitionPropertyEntity entity = new PartitionPropertyEntity(partitionId,
          entry.getKey(), entry.getValue());
      encryptPartitionPropertyEntityIfNeeded(entity);
      partitionProperties.add(entity);
    }
    this.partitionPropertyEntityRepository.performTransaction(repository -> {
      repository.saveAll(partitionProperties);
      return true;
    });
    return getPartition(partitionId);
  }

  private void encryptPartitionPropertyEntityIfNeeded(PartitionPropertyEntity entity) {
    if (entity.isSensitive()) {
      String propertyValue = entity.getValue().toString();
      try {
        entity.setValue(this.kmsClient.encryptString(propertyValue));
      } catch (IOException e) {
        throw new AppException(HttpStatus.SC_INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_REASON,
            e.getMessage());
      }
    }
  }

  @Override
  public PartitionInfo updatePartition(String partitionId, PartitionInfo partitionInfo) {
    if (partitionInfo.getProperties().containsKey("id")) {
      this.auditLogger.updatedPartitionSecretFailure(Collections.singletonList(partitionId));
      throw new AppException(HttpStatus.SC_BAD_REQUEST, "can not update id",
          "the field id can not be updated");
    }

    if (!this.partitionPropertyEntityRepository.findByPartitionId(partitionId).isPresent()) {
      this.auditLogger.updatedPartitionSecretFailure(Collections.singletonList(partitionId));
      throw new AppException(HttpStatus.SC_NOT_FOUND, UNKNOWN_ERROR_REASON,
          "An attempt to update not existing partition.");
    }

    List<PartitionPropertyEntity> partitionProperties = new ArrayList<>();
    for (Map.Entry<String, Property> entry : partitionInfo.getProperties().entrySet()) {
      PartitionPropertyEntity entity = this.partitionPropertyEntityRepository
          .findByPartitionIdAndName(partitionId, entry.getKey());
      if (Objects.nonNull(entity)) {
        entity.setSensitive(entry.getValue().isSensitive());
        entity.setValue(entry.getValue().getValue());
      } else {
        entity = new PartitionPropertyEntity(partitionId, entry.getKey(), entry.getValue());
      }
      encryptPartitionPropertyEntityIfNeeded(entity);
      partitionProperties.add(entity);
    }
    this.partitionPropertyEntityRepository.performTransaction(repository -> {
      repository.saveAll(partitionProperties);
      return true;
    });
    return getPartition(partitionId);
  }

  @Override
  public PartitionInfo getPartition(String partitionId) {
    PartitionInfo partitionInfo = getEncryptedPartition(partitionId);
    for (Property property : partitionInfo.getProperties().values()) {
      decryptPartitionPropertyIfNeeded(property);
    }
    return partitionInfo;
  }

  private PartitionInfo getEncryptedPartition(String partitionId) {
    if (!this.partitionPropertyEntityRepository.findByPartitionId(partitionId).isPresent()) {
      this.auditLogger.readPartitionFailure(Collections.singletonList(partitionId));
      throw new AppException(HttpStatus.SC_NOT_FOUND, UNKNOWN_ERROR_REASON,
          "Partition does not exist.");
    }
    List<PartitionPropertyEntity> partitionPropertiesList = this.partitionPropertyEntityRepository
        .findByPartitionId(partitionId).get();
    PartitionInfo partitionInfo = new PartitionInfo();
    Map<String, Property> partitionInfoProperties = new HashMap<>();
    for (PartitionPropertyEntity entity : partitionPropertiesList) {
      partitionInfoProperties
          .put(entity.getName(), new Property(entity.getSensitive(), entity.getValue()));
    }
    partitionInfo.setProperties(partitionInfoProperties);

    return partitionInfo;
  }

  private void decryptPartitionPropertyIfNeeded(Property property) {
    if (property.isSensitive()) {
      String propertyValue = property.getValue().toString();
      try {
        property.setValue(this.kmsClient.decryptString(propertyValue));
      } catch (IOException e) {
        throw new AppException(HttpStatus.SC_INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_REASON,
            e.getMessage());
      }
    }
  }

  @Transactional
  @Override
  public boolean deletePartition(String partitionId) {
    if (!this.partitionPropertyEntityRepository.findByPartitionId(partitionId).isPresent()) {
      this.auditLogger.deletedPartitionFailure(Collections.singletonList(partitionId));
      throw new AppException(HttpStatus.SC_NOT_FOUND, UNKNOWN_ERROR_REASON,
          "An attempt to delete not existing partition.");
    }
    this.partitionPropertyEntityRepository.deleteByPartitionId(partitionId);
    return true;
  }

  @Transactional
  @Override
  public List<String> getAllPartitions() {
    List<String> allPartitions = this.partitionPropertyEntityRepository.getAllPartitions();
    return (allPartitions.isEmpty() ? null : allPartitions);
  }
}
