package com.lynas.controller

import com.lynas.model.Organization
import com.lynas.service.OrganizationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by LynAs on 8/13/2016
 */

@RestController
@RequestMapping("organizations")
class OrganizationController(val organizationService: OrganizationService) {

    @PostMapping
    fun createNewOrganization(@RequestBody organization: Organization): Organization {
        organizationService.create(organization)
        return organization
    }



}