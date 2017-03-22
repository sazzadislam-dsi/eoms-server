package com.lynas.repo

import com.lynas.model.Organization
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface OrganizationRepository : GraphRepository<Organization?> {
    fun findListByName(name: String): List<Organization>
    fun findByName(name: String): Organization?
}
