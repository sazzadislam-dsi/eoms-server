package com.lynas.service

import com.lynas.exception.ConstraintsViolationException
import com.lynas.model.AppUser
import com.lynas.model.util.SpringSecurityUser
import com.lynas.repo.AppUserRepository
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class AppUserService(private val repository: AppUserRepository) : UserDetailsService {

    fun create(appUser: AppUser): AppUser {
        try {
            return repository.save(appUser)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ConstraintsViolationException("unable to create user with given entity : " + appUser.toString())
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val appUser = repository.findByUsername(username)
        return if (appUser == null) {
            throw UsernameNotFoundException("No user found with given username : " + username)
        } else {
            SpringSecurityUser(
                    appUser.username,
                    appUser.password, null, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(appUser.authorities)
            )
        }
    }
}
