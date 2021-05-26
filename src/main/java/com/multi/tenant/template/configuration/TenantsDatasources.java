package com.multi.tenant.template.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Data
@ConfigurationProperties(prefix = "tenants")
@Profile("tenants")
public class TenantsDatasources {
    private HashMap<String, HashMap<String, String>> datasources;
}