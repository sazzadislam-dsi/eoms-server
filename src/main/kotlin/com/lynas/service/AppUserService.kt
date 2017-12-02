package com.lynas.service

import com.lynas.exception.ConstraintsViolationException
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.AppUser
import com.lynas.repo.AppUserRepository
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
@ComponentScan(scopedProxy = ScopedProxyMode.INTERFACES)
class AppUserService(val appUserRepository: AppUserRepository) {

    @Transactional
    fun create(appUser: AppUser): AppUser {
        try {
            return appUserRepository.save(appUser)
        } catch (e: Exception) {
            throw ConstraintsViolationException("Unable to save with given entity : " + appUser.toString())
        }
    }

    @Transactional
    fun findByUserName(username: String): AppUser {
        return appUserRepository.findByUsername(username)
                ?: throw EntityNotFoundException("username not found with username : " + username)
    }
}