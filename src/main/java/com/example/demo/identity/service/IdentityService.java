package com.example.demo.identity.service;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {

    @Value("${openstack.endpoint}")
    private String endpoint;

    @Value("${openstack.userId}")
    private String userId;

    @Value("${openstack.secret}")
    private String secret;

    @Value("${openstack.projectName}")
    private String projectName;

    @Value("${openstack.domainName}")
    private String domainName;

    public OSClient.OSClientV3 getOSClientV3() {
        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(userId, secret, Identifier.byName(domainName))
                .scopeToProject(Identifier.byName(projectName), Identifier.byName(domainName))
                .authenticate();
        return os;
    }
}
