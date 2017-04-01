package com.lynas.service

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
open class AppUserService(val appUserRepository: AppUserRepository) {

    @Transactional
    open fun save(appUser: AppUser): AppUser? = appUserRepository.save(appUser)

    @Transactional
    open fun findByUserName(username: String): AppUser? = appUserRepository.findByUsername(username)
}