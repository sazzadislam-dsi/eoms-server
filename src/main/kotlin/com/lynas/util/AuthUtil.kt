package com.lynas.util

import com.lynas.config.security.JwtTokenUtil
import com.lynas.model.Organization
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AuthUtil(val tokenUtil: JwtTokenUtil) {
    @Value("\${jwt.header}")
    private val tokenHeader: String = "Authorization"

    fun getOrganizationFromToken(request: HttpServletRequest): Organization
            = tokenUtil.getOrganizationFromToken(request.getHeader(tokenHeader))

    fun getOrganizationIdFromToken(request: HttpServletRequest): Long
            = getOrganizationFromToken(request).id!!
}