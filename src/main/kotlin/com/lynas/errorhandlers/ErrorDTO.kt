package com.lynas.errorhandlers

import org.springframework.http.HttpStatus


data class ErrorDTO (val code: Int, val message: String?, val httpStatus: HttpStatus)