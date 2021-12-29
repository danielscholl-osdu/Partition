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

package org.opengroup.osdu.partition.provider.gcp.config;

import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
@Setter
public class PropertiesConfiguration {

    private String googleAudiences;

    private List<String> partitionAdminAccounts;

    private String googleCloudProject;

    private int cacheExpiration;

    private int cacheMaxSize;

    private String serviceAccountTail;

    private String partitionPropertyKind;

    private String partitionNamespace;

    @PostConstruct
    public void setUp() {
        if (Objects.isNull(serviceAccountTail) || serviceAccountTail.isEmpty()) {
            this.serviceAccountTail = googleCloudProject + ".iam.gserviceaccount.com";
        }
    }

}
