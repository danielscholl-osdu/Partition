package org.opengroup.osdu.partition.provider.aws.service;

import org.opengroup.osdu.partition.provider.interfaces.IHealthCheckService;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckServiceImpl implements IHealthCheckService {

    @Override
    public void performReadinessCheck() {

    }
}
