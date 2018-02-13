package com.lynas.controller

import com.lynas.model.ContactInformation
import com.lynas.service.ContactInformationService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("contactInformations")
class ContactController(val contactInformationService: ContactInformationService) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long, request: HttpServletRequest) = contactInformationService.findById(id)

    @PatchMapping("/{id}")
    fun updateContactInfo(@PathVariable id: Long, @RequestBody contactInformation: ContactInformation): ContactInformation {
        val contactInfo = contactInformationService.findById(id)
        contactInfo.apply {
            name = contactInformation.name
            address = contactInformation.address
            phone_1 = contactInformation.phone_1
            phone_2 = contactInformation.phone_2
            phone_3 = contactInformation.phone_3
            contactType = contactInformation.contactType
        }
        contactInformationService.create(contactInfo)
        return contactInfo
    }
}