package com.lynas.controller

import com.lynas.dto.OrganizationDTO
import com.lynas.service.OrganizationService
import com.lynas.util.responseCreated
import org.springframework.http.ResponseEntity
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
    fun createNewOrganization(@RequestBody organization: OrganizationDTO): ResponseEntity<*>
        = responseCreated(organizationService.create(organization.toOrganization()))

}