package com.lynas.controller.rest

import com.lynas.model.ContactInformation
import com.lynas.service.ContactInformationService
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("contactInformations")
class ContactInformationRestController (val contactInformationService: ContactInformationService) {

    private val logger = getLogger(ContactInformationRestController::class.java)

    @PatchMapping
    fun patch(@RequestBody contactInformation: ContactInformation): ResponseEntity<*> {
        logger.info("contact info is going to update :: {}", contactInformation)
        if (null != contactInformation.id) {
            var contactInfo = contactInformationService.findById(contactInformation.id as Long)
            if (null != contactInfo) {
                contactInfo.apply {
                    name = contactInformation.name
                    address = contactInformation.address
                    phone_1 = contactInformation.phone_1
                    phone_2 = contactInformation.phone_2
                    phone_3 = contactInformation.phone_3
                    contactType = contactInformation.contactType
                }
                contactInformationService.create(contactInfo)
                return responseOK(contactInfo)
            }
        }
        return responseOK(contactInformation)
    }
}