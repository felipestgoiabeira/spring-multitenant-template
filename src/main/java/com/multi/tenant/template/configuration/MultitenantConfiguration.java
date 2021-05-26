package com.multi.tenant.template.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultitenantConfiguration {

    @Autowired
    private DataSourceProperties properties;

    @Autowired
    private TenantsDatasources datasources;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        Map<Object,Object> resolvedDataSources = new HashMap<>();

        for(Map.Entry<String, HashMap<String, String>> entry : datasources.getDatasources().entrySet()) {
            HashMap<String, String > tenantProperties = entry.getValue();
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            try {
                String tenantId =  entry.getKey();
                dataSourceBuilder.driverClassName(properties.getDriverClassName())
                        .url(tenantProperties.get("url"))
                        .username(tenantProperties.get("username"))
                        .password(tenantProperties.get("password"));
                if(properties.getType() != null) {
                    dataSourceBuilder.type(properties.getType());
                }

                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        // Setting the default datasource
        MultitenantDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(defaultDataSource());
        dataSource.setTargetDataSources(resolvedDataSources);

        // Call this to finalize the initialization of the data source.
        dataSource.afterPropertiesSet();

        return dataSource;
    }

    private DataSource defaultDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(properties.getDriverClassName());
        dataSourceBuilder.url(properties.getUrl());
        dataSourceBuilder.username(properties.getUsername());
        dataSourceBuilder.password(properties.getPassword());

        if(properties.getType() != null) {
            dataSourceBuilder.type(properties.getType());
        }

        return dataSourceBuilder.build();
    }
}
