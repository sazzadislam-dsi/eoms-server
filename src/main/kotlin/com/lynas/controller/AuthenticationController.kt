package com.lynas.controller

import com.lynas.config.security.JwtTokenUtil
import com.lynas.dto.AuthenticationRequestDTO
import com.lynas.dto.AuthenticationResponseDTO
import com.lynas.service.AppUserService
import org.springframework.mobile.device.Device
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Created by sazzad on 8/23/16
 */

@RestController
@RequestMapping("auth")
class AuthenticationController(val appUserService: AppUserService,
                               val authenticationManager: AuthenticationManager,
                               val jwtTokenUtil: JwtTokenUtil) {


    @PostMapping("/login")
    fun login(@Valid @RequestBody authenticationRequestDTO: AuthenticationRequestDTO, device: Device)
            : AuthenticationResponseDTO {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.username, authenticationRequestDTO.password))
        SecurityContextHolder.getContext().authentication = authentication
        val appUser = appUserService.findByUsername(username = authenticationRequestDTO.username)
        val userDetails = appUserService.loadUserByUsername(authenticationRequestDTO.username)
        val token = jwtTokenUtil.generateToken(userDetails, device, appUser.organization)
        return AuthenticationResponseDTO(token)

    }

}

