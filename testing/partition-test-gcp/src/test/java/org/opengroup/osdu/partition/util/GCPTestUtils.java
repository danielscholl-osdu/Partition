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

package org.opengroup.osdu.partition.util;

import com.google.common.base.Strings;

public class GCPTestUtils extends TestUtils {

  @Override
  public String getAccessToken() throws Exception {
    if (Strings.isNullOrEmpty(token)) {
      String serviceAccountFile = System
          .getProperty("INTEGRATION_TESTER", System.getenv("INTEGRATION_TESTER"));
      String audience = System.getProperty("INTEGRATION_TEST_AUDIENCE",
          System.getenv("INTEGRATION_TEST_AUDIENCE"));
      if (Strings.isNullOrEmpty(audience)) {
        audience = "245464679631-ktfdfpl147m1mjpbutl00b3cmffissgq.apps.googleusercontent.com";
      }
      token = new GoogleServiceAccount(serviceAccountFile).getAuthToken(audience);
    }
    return "Bearer " + token;
  }

  @Override
  public String getNoAccessToken() throws Exception {
    if (Strings.isNullOrEmpty(noAccessToken)) {
      String serviceAccountFile = System.getProperty("NO_DATA_ACCESS_TESTER",
          System.getenv("NO_DATA_ACCESS_TESTER"));
      String audience = System.getProperty("INTEGRATION_TEST_AUDIENCE",
          System.getenv("INTEGRATION_TEST_AUDIENCE"));
      if (Strings.isNullOrEmpty(audience)) {
        audience = "245464679631-ktfdfpl147m1mjpbutl00b3cmffissgq.apps.googleusercontent.com";
      }
      noAccessToken = new GoogleServiceAccount(serviceAccountFile).getAuthToken(audience);
    }
    return "Bearer " + noAccessToken;
  }
}
