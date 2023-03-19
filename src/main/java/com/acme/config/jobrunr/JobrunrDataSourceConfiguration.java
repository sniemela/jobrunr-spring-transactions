package com.acme.config.jobrunr;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.spring.autoconfigure.JobRunrProperties;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.StorageProviderUtils;
import org.jobrunr.storage.sql.common.SqlStorageProviderFactory;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class JobrunrDataSourceConfiguration {

    @Bean(name = {"storageProvider"}, destroyMethod = "close")
    @DependsOnDatabaseInitialization
    public StorageProvider sqlStorageProvider(DataSource dataSource, JobMapper jobMapper, JobRunrProperties properties) {
        String tablePrefix = properties.getDatabase().getTablePrefix();
        StorageProviderUtils.DatabaseOptions databaseOptions = properties.getDatabase().isSkipCreate() ? StorageProviderUtils.DatabaseOptions.SKIP_CREATE : StorageProviderUtils.DatabaseOptions.CREATE;
        StorageProvider storageProvider = SqlStorageProviderFactory.using(getDataSource(dataSource), tablePrefix, databaseOptions);
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

    private DataSource getDataSource(DataSource dataSource) {
        return new JobrunrTransactionAwareDataSourceProxy(dataSource);
    }
}
