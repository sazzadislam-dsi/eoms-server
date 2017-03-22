package com.lynas.controller.view

import com.lynas.service.ContactInformationService
import com.lynas.util.getLogger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by seal on 11/25/2016.
 */
@Controller
@RequestMapping("contactInformation")
class ContactInfoController constructor(val contactInfoService: ContactInformationService) {

    val logger = getLogger(ContactInfoController::class.java)

    @GetMapping("/create/")
    fun contactInfoCreate(@RequestParam userId: Long, model: Model): String {
        logger.info("hit contactInfoCreate method UserID :: {}", userId)
        model.addAttribute("userId", userId)
        return "contactInfoCreate"
    }

    @GetMapping("/detail/{contactId}")
    fun contactInfoDetail(@PathVariable contactId: Long, model: Model): String {
        logger.info("hit method contactInfoDetail with id {}", contactId)
        var contactInfo = contactInfoService.findById(contactId)
        model.addAttribute("contact", contactInfo)
        return "contactInfoDetail"
    }

    @GetMapping("/{contactId}/update/{studentId}/student")
    fun contactUpdate(@PathVariable contactId: Long, @PathVariable studentId: Long, model: Model): String {
        var contact = contactInfoService.findById(contactId)
        logger.info("return for update contact info for id {}", contactId)
        model.addAttribute("contact", contact)
        model.addAttribute("studentId", studentId)
        return "contactInfoUpdate"
    }
}