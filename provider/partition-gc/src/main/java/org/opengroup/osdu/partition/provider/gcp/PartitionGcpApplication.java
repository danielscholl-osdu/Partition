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

package org.opengroup.osdu.partition.provider.gcp;

import org.opengroup.osdu.core.di.GcpPartitionClientFactory;
import org.opengroup.osdu.core.gcp.osm.translate.postgresql.PgTenantOsmDestinationResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {"org.opengroup.osdu"}, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    GcpPartitionClientFactory.class,
    PgTenantOsmDestinationResolver.class
})
)
@SpringBootApplication
@PropertySource("classpath:swagger.properties")
public class PartitionGcpApplication {

  public static void main(String[] args) {
    SpringApplication.run(PartitionGcpApplication.class, args);
  }
}
