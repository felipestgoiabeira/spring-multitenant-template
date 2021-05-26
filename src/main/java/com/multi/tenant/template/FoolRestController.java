package com.multi.tenant.template;

import com.multi.tenant.template.configuration.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/show-current-tenant")
public class FoolRestController {
    @GetMapping
    public String showCurrentTenant() {
        return "The tenantId is: " + TenantContext.getCurrentTenant().toString();
    }
}
