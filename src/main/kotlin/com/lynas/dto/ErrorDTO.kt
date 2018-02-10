package com.lynas.dto

import org.springframework.http.HttpStatus


data class ErrorDTO (val code: Int, val message: String?, val httpStatus: HttpStatus)

data class ErrorObject(
        val receivedObject: Any,
        val errorField: Any,
        val errorMessage: Any,
        val errorSuggestion: Any
)