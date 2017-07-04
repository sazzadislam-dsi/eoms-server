package com.lynas.config

import com.lynas.util.AppConstant
import com.lynas.util.getLogger
import org.apache.log4j.Logger
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.time.LocalDate
import java.time.LocalTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by sazzad on 10/3/16
 */

@Component
class InterceptorConfig : HandlerInterceptorAdapter() {

    val logger = getLogger(InterceptorConfig::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        logger.info("URL path [{}]", request.requestURI)
        request.session.setAttribute("currentYear", LocalDate.now().year)
        when (request.requestURI) {
            "/", "/login", "/logout" -> return true
        }
        if (null == request.session.getAttribute(AppConstant.organization)) {
            response.sendRedirect("/")
            return false
        }
        return true
    }
}