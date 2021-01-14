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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsFactory;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsService;
import org.opengroup.osdu.core.common.http.HttpResponse;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.AppException;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.partition.provider.interfaces.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class AuthorizationService implements IAuthorizationService {

	private static final String ERROR_REASON = "Access denied";
	private static final String ERROR_MSG = "The user is not authorized to perform this action";

	private static final String PARTITION_ADMIN_ROLE = "service.partition.admin";

	private final DpsHeaders headers;

    private final org.opengroup.osdu.core.common.provider.interfaces.IAuthorizationService authorizationService;

	@Override
	public boolean isDomainAdminServiceAccount() {
		try {
			authorizationService.authorizeAny(headers,PARTITION_ADMIN_ROLE);
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Authentication Failure",
				e.getMessage(), e);
		}
		return true;
	}
}
