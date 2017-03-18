package com.lynas.repo

import com.lynas.model.AppUser
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

/**
 * Created by sazzad on 8/17/16
 */

@Repository
interface AppUserRepository : GraphRepository<AppUser> {
    fun findByUsername(username: String): AppUser
}