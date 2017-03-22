package com.lynas.model.response

/**
 * Created by lynas on 10/16/2016
 */

data class ErrorObject(
        val receivedObject: Any,
        val errorField : Any,
        val errorMessage: Any,
        val errorSuggestion: Any
)