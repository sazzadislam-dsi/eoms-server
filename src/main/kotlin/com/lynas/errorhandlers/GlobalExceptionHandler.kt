package com.lynas.errorhandlers

import com.lynas.util.getLogger
import com.lynas.util.responseError
import org.neo4j.ogm.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class GlobalExceptionHandler {

    val logger = getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun duplicateCourseExceptionHandler(exception: RuntimeException,
                                        request: HttpServletRequest) : ResponseEntity<*> {
        logger.error("URL [{}], message [{}]", request.requestURI, exception.message)

        return responseError(ErrorDTO(message = exception.message, code = HttpStatus.NOT_FOUND.value()))
    }
}