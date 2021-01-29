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

package org.opengroup.osdu.partition.api;

import static org.junit.Assert.assertEquals;

import com.sun.jersey.api.client.ClientResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengroup.osdu.partition.util.GCPTestUtils;

public class TestCreatePartition extends CreatePartitionTest {

  @Override
  @Before
  public void setup() throws Exception {
    this.testUtils = new GCPTestUtils();
  }

  @Override
  @After
  public void tearDown() throws Exception {
    this.testUtils = null;
  }

  @Override
  @Test
  public void should_return40XResponseCode_when_makingRequest_withInvalidPayload() throws Exception {
    String invalidPayload = "invalidPayload";
    ClientResponse response = descriptor.runWithCustomPayload(getId(), invalidPayload, testUtils.getAccessToken());
    assertEquals(400, response.getStatus());
  }
}
