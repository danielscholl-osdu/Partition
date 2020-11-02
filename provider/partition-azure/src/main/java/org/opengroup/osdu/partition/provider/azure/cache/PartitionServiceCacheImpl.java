package org.opengroup.osdu.partition.provider.azure.cache;

import org.opengroup.osdu.core.common.cache.VmCache;
import org.opengroup.osdu.partition.model.PartitionInfo;
import org.opengroup.osdu.partition.provider.interfaces.IPartitionServiceCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Qualifier("partitionServiceCache")
public class PartitionServiceCacheImpl extends VmCache<String, PartitionInfo> implements IPartitionServiceCache<String, PartitionInfo> {

    public PartitionServiceCacheImpl() {
        super(5 * 60, 1000);
    }
}
