package com.lynas.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.text.ParseException


class DuplicateEntryException(message: String) : IllegalStateException(message)

class EntityNotFoundException(message: String) : NullPointerException(message)

class DateFormatParseException(message: String, errorOffset: Int): ParseException(message, errorOffset)

class ConstraintsViolationException(message: String) : Exception(message)


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class UnauthorizedAccessException(message: String) : Exception(message)