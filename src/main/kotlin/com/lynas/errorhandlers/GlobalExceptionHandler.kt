package com.lynas.errorhandlers

import com.lynas.exception.DateFormatParseException
import com.lynas.exception.DuplicateEntryException
import com.lynas.exception.EntityNotFoundForGivenIdException
import com.lynas.util.getLogger
import com.lynas.util.responseError
import com.lynas.util.responseNotFound
import org.neo4j.ogm.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    val logger = getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(exception: RuntimeException, request: HttpServletRequest) : ResponseEntity<*> {
        logger.error("URL [{}], message [{}]", request.requestURI, exception.message)
        return responseError(ErrorDTO(message = exception.message, code = HttpStatus.NOT_FOUND.value(), httpStatus = HttpStatus.NOT_FOUND))
    }

    @ExceptionHandler(EntityNotFoundForGivenIdException::class)
    fun entityNotFoundForGivenIdExceptionHandler(exception: EntityNotFoundForGivenIdException,
                                                 request: HttpServletRequest): ResponseEntity<*> {
        logger.error("URL [{}], message [{}]", request.requestURI, exception.message)
        return responseNotFound(ErrorDTO(message = exception.message, code = HttpStatus.NOT_FOUND.value(), httpStatus = HttpStatus.NOT_FOUND))
    }

    @ExceptionHandler(DuplicateEntryException::class)
    fun illegalStateExceptionHandler(exception: DuplicateEntryException,
                                                 request: HttpServletRequest): ResponseEntity<*> {
        logger.error("URL [{}], message [{}]", request.requestURI, exception.message)
        return responseNotFound(ErrorDTO(message = exception.message, code = HttpStatus.BAD_REQUEST.value(), httpStatus = HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(DateFormatParseException::class)
    fun dateFormatParseExceptionHandler(exception: DateFormatParseException,
                                                 request: HttpServletRequest): ResponseEntity<*> {
        logger.error("URL [{}], message [{}], errorOffset [{}]", request.requestURI, exception.message, exception.errorOffset)
        return responseNotFound(ErrorDTO(message = exception.message, code = HttpStatus.BAD_REQUEST.value(), httpStatus = HttpStatus.BAD_REQUEST))
    }

}