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

package org.opengroup.osdu.partition.provider.gcp.security;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.opengroup.osdu.core.common.cache.ICache;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsAndCacheService;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsFactory;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsService;
import org.opengroup.osdu.core.common.http.HttpResponse;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.AppException;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.core.common.model.storage.RecordMetadata;
import org.opengroup.osdu.core.common.util.Crc32c;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntitlementsAndCacheServiceImpl implements IEntitlementsAndCacheService {

  private static final String ERROR_REASON = "Access denied";
  private static final String ERROR_MSG = "The user is not authorized to perform this action";

  private final IEntitlementsFactory factory;

  private final ICache<String, Groups> cache;

  private final JaxRsDpsLog logger;

  @Override
  public String authorize(DpsHeaders headers, String... roles) {
    Groups groups = this.getGroups(headers);
    if (groups.any(roles)) {
      return groups.getDesId();
    } else {
      throw new AppException(HttpStatus.SC_UNAUTHORIZED, ERROR_REASON, ERROR_MSG);
    }
  }

  @Override
  public boolean isValidAcl(DpsHeaders headers, Set<String> acls) {
    return false;
  }

  @Override
  public boolean hasOwnerAccess(DpsHeaders headers, String[] ownerList) {
    return false;
  }

  @Override
  public List<RecordMetadata> hasValidAccess(List<RecordMetadata> recordsMetadata,
      DpsHeaders headers) {
    return null;
  }

  protected Groups getGroups(DpsHeaders headers) {
    String cacheKey = getGroupCacheKey(headers);
    Groups groups = this.cache.get(cacheKey);

    if (groups == null) {
      IEntitlementsService service = this.factory.create(headers);
      try {
        groups = service.getGroups();
        this.cache.put(cacheKey, groups);
        this.logger.info("Entitlements cache miss");

      } catch (EntitlementsException e) {
        HttpResponse response = e.getHttpResponse();
        this.logger.error(String.format("Error requesting entitlements service %s", response));
        throw new AppException(e.getHttpResponse().getResponseCode(), ERROR_REASON, ERROR_MSG, e);
      }
    }

    return groups;
  }

  protected static String getGroupCacheKey(DpsHeaders headers) {
    String key = String
        .format("entitlement-groups:%s:%s", headers.getPartitionIdWithFallbackToAccountId(),
            headers.getAuthorization());
    return Crc32c.hashToBase64EncodedString(key);
  }
}
