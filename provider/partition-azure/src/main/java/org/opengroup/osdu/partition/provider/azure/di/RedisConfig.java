package org.opengroup.osdu.partition.provider.azure.di;

import org.opengroup.osdu.azure.cache.RedisAzureCache;
import org.opengroup.osdu.azure.di.RedisAzureConfiguration;
import org.opengroup.osdu.partition.model.PartitionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import java.util.List;

@Configuration
public class RedisConfig {
    @Value("${redis.port}")
    private int port;

    @Value("${redis.expiration}")
    private int expiration;

    @Value("${redis.database}")
    private int database;

    @Value("${redis.connection.timeout}")
    private long timeout;

    @Bean
    public RedisAzureCache<String, PartitionInfo> partitionServiceCache() {
        RedisAzureConfiguration redisAzureConfiguration = new RedisAzureConfiguration(database, expiration, port, timeout);
        return new RedisAzureCache<>(String.class, PartitionInfo.class, redisAzureConfiguration);
    }

    @Bean
    public RedisAzureCache<String, List<String>> partitionListCache() {
        RedisAzureConfiguration redisAzureConfiguration = new RedisAzureConfiguration(database, expiration, port, timeout);
        return new RedisAzureCache(String.class, List.class, redisAzureConfiguration);
    }
}
