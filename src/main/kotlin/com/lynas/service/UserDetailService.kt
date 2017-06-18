package com.lynas.service

import com.lynas.model.AppUser
import com.lynas.model.util.SpringSecurityUser
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by lynas on 9/9/2016
 */
@Service("UserDetailsService")
@ComponentScan(scopedProxy = ScopedProxyMode.INTERFACES)
class UserDetailService(val appUserService: AppUserService) : UserDetailsService {


    override fun loadUserByUsername(userName: String): UserDetails {
        val appUser:AppUser? = appUserService.findByUserName(userName)
        if (appUser == null) {
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