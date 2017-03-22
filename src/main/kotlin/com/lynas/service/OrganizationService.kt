package com.lynas.service

import com.lynas.model.Organization
import com.lynas.repo.OrganizationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class OrganizationService(val orgRepo: OrganizationRepository) {

    @Transactional
    open fun save(org: Organization) {
        orgRepo.save(org)
    }


    @Transactional
    open fun readByID(id: Long): Organization? {
        return orgRepo.findOne(id)
    }


    @Transactional
    open fun readByOtherName(name: String): List<Organization> {
        return orgRepo.findListByName(name)
    }

    @Transactional
    open fun findByName(name: String): Organization? {
        return orgRepo.findByName(name)
    }


}