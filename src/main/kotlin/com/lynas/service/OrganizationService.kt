package com.lynas.service

import com.lynas.model.Organization
import com.lynas.repo.OrganizationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class OrganizationService(val orgRepo: OrganizationRepository) {

    @Transactional
    fun create(org: Organization): Organization {
        return orgRepo.save(org)
    }


    @Transactional
    fun readByID(id: Long): Organization? {
        return orgRepo.findOne(id)
    }


    @Transactional
    fun readByOtherName(name: String): List<Organization> {
        return orgRepo.findListByName(name)
    }

    @Transactional
    fun findByName(name: String): Organization? {
        return orgRepo.findByName(name)
    }


}