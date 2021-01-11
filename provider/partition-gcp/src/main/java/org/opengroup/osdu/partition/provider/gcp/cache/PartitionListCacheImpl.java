/*
  Copyright 2020 Google LLC
  Copyright 2020 EPAM Systems, Inc

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

package org.opengroup.osdu.partition.provider.gcp.cache;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.opengroup.osdu.core.common.cache.ICache;
import org.opengroup.osdu.partition.provider.interfaces.IPartitionServiceCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("partitionListCache")
@RequiredArgsConstructor
public class PartitionListCacheImpl implements IPartitionServiceCache<String, List<String>> {

  private final ICache<String, List<String>> cache;

  @Override
  public void put(String s, List<String> o) {
    this.cache.put(s, o);
  }

  @Override
  public List<String> get(String s) {
    return this.cache.get(s);
  }

  @Override
  public void delete(String s) {
    this.cache.delete(s);
  }

  @Override
  public void clearAll() {
    this.cache.clearAll();
  }

}
