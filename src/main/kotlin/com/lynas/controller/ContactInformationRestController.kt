package com.lynas.controller

import com.lynas.exception.EntityNotFoundException
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
class ContactInformationRestController(val contactInformationService: ContactInformationService) {

    private val logger = getLogger(this.javaClass)

    @PatchMapping
    fun patch(@RequestBody contactInformation: ContactInformation): ResponseEntity<*> {
        logger.info("contact info is going to update :: {}", contactInformation)
        // TODO contactInformation.id validation check in bean validation
        if (null != contactInformation.id) {
            val contactInfo = contactInformationService.findById(contactInformation.id as Long) ?: throw EntityNotFoundException(
                    message = "Contact Info not found for id [${contactInformation.id}]"
            )
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
        return responseOK(contactInformation)
    }
}