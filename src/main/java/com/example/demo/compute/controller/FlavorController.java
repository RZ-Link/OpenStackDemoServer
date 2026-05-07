package com.example.demo.compute.controller;

import com.example.demo.compute.response.Flavor;
import com.example.demo.compute.response.FlavorListResponse;
import com.example.demo.identity.service.IdentityService;
import com.example.demo.r.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flavor")
public class FlavorController {
    @Autowired
    private IdentityService identityService;

    @GetMapping("/list")
    public R<FlavorListResponse> list() {
        var os = identityService.getOSClientV3();
        var flavors = os.compute().flavors().list();

        List<Flavor> result = new ArrayList<>();
        for (var flavor : flavors) {
            Flavor temp = new Flavor();
            temp.setId(flavor.getId());
            temp.setName(flavor.getName());
            temp.setVcpus(flavor.getVcpus());
            temp.setRam(flavor.getRam());
            temp.setDisk(flavor.getDisk());
            result.add(temp);
        }
        FlavorListResponse flavorListResponse = new FlavorListResponse();
        flavorListResponse.setFlavors(result);

        return R.ok(flavorListResponse);

    }
}
