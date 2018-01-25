package com.lynas.controller

import com.lynas.model.Organization
import com.lynas.service.OrganizationService
import com.lynas.util.getLogger
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

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody organization: Organization): Organization {
        logger.info("Hit Organization RestController with {}", organization.toString())
        organizationService.create(organization)
        return organization
    }



}