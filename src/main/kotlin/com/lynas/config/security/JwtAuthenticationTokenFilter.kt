package com.lynas.config.security

import com.lynas.model.util.SpringSecurityUser
import com.lynas.util.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationTokenFilter(private val jwtTokenUtil: JwtTokenUtil) : OncePerRequestFilter() {

    val log = getLogger(this.javaClass)
    @Value("\${jwt.header}")
    private val tokenHeader: String? = null

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
                                  filterChain: FilterChain) {
        logRequest(request)
        val authToken = request.getHeader(this.tokenHeader)
        val username = jwtTokenUtil.getUsernameFromToken(authToken)
        if (username != null && !jwtTokenUtil.isTokenExpired(authToken)
                && SecurityContextHolder.getContext().authentication == null) {
            val rolesString = jwtTokenUtil.getUserRoleFromToken(authToken)
            val authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesString)
            val authUser = SpringSecurityUser(username, authorities)
            val authentication = UsernamePasswordAuthenticationToken(authUser, null, authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }


    private fun logRequest(request: HttpServletRequest) {
        log.info("**************URL************")
        log.info(request.requestURI)
        log.info("**************HEADER************")
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val key = headerNames.nextElement() as String
            val value = request.getHeader(key)
            log.info("header $key --- $value")
        }
        log.info("**************************")
    }
}
