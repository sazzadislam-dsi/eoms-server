package com.lynas.repo

import com.lynas.model.Organization
import org.springframework.data.repository.CrudRepository

/**
 * Created by sazzad on 7/19/16
 */

interface OrganizationRepositorySpring : CrudRepository<Organization, String> {

    fun findByName(name: String): List<Organization>
}
