// Copyright 2017-2020, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.partition.api.descriptor;

import org.opengroup.osdu.partition.util.RestDescriptor;
import org.springframework.web.bind.annotation.RequestMethod;

public class CreatePartitionDescriptor extends RestDescriptor {

    private String partitionId;

    @Override
    public String getPath() {
        return "api/partition/v1/partitions/" + this.arg();
    }

    @Override
    public String getHttpMethod() {
        return RequestMethod.POST.toString();
    }

    @Override
    public String getValidBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        sb.append("  \"properties\": {")
                .append("\"id\": \"").append(this.arg()).append("\",")
                .append("\"elasticPassword\": \"test-password\",")
                .append("\"serviceBusConnection\": \"test-service-bus-connection\",")
                .append("\"serviceprincipalAppId\": \"test-sp\",")
                .append("\"serviceBusNamespace\": \"test-service-bus\",")
                .append("\"elasticUsername\": \"test-password\",")
                .append("\"cosmosEndpoint\": \"test-comos-endpoint\",")
                .append("\"elasticEndpoint\": \"test-elastic-endpoint\",")
                .append("\"cosmosPrimaryKey\": \"test-cosmos-primary-key\",")
                .append("\"groups\": \"test-groups\",")
                .append("\"storageAccount\": \"test-storage-account\",")
                .append("\"complianceRuleSet\": \"test-compliance-ruleSet\",")
                .append("\"cosmosConnection\": \"test-cosmos-connection\",")
                .append("\"storageAccountKey\": \"test-storage-accountKey\"")
                .append("}\n")
                .append("}");
        return sb.toString();
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }
}
