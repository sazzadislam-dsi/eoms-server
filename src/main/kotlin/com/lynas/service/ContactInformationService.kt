package com.lynas.service

import com.lynas.model.ContactInformation
import com.lynas.repo.ContactInformationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class ContactInformationService(val contactInformationRepository: ContactInformationRepository) {

    @Transactional
    fun create(contactInformation: ContactInformation) {
        contactInformationRepository.save(contactInformation)
    }

    @Transactional
    fun findById(id: Long): ContactInformation? {
        return contactInformationRepository.findOne(id)
    }
}