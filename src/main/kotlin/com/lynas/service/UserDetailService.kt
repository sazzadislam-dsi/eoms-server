package com.lynas.service

import com.lynas.config.SpringSecurityUser
import com.lynas.model.AppUser
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by com.lynas on 9/9/2016
 */
@Service
open class UserDetailService(var appUserService: AppUserService) : UserDetailsService {

    override fun loadUserByUsername(userName: String): UserDetails {
        val appUser: AppUser? = appUserService.loadUserByUsername(userName)
        if (null == appUser) {
            throw UsernameNotFoundException(String.format("No user found with username '%s'" + userName))
        } else {
            return SpringSecurityUser(
                    appUser.id,
                    appUser.username,
                    appUser.password,
                    null,
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(appUser.authorities)
            )
        }

    }
}