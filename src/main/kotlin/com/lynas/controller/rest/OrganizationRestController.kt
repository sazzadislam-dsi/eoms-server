package com.lynas.controller.rest

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
class OrganizationRestController (val organizationService: OrganizationService) {

    val logger = getLogger(OrganizationRestController::class.java)

    @PostMapping
    fun post(@RequestBody organization: Organization): Organization {
        logger.info("Hit Organization RestController with {}", organization.toString())
        organizationService.save(organization)
        return organization
    }



}