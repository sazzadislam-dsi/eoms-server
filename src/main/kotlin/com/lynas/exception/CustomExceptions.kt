package com.lynas.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.text.ParseException

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class DuplicateEntryException(message: String) : IllegalStateException(message)

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class EntityNotFoundException(message: String) : NullPointerException(message)

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class DateFormatParseException(message: String, errorOffset: Int): ParseException(message, errorOffset)

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class ConstraintsViolationException(message: String) : Exception(message)

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class UnauthorizedAccessException(message: String) : Exception(message)