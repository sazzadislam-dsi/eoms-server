package com.lynas.exception

import java.text.ParseException


class DuplicateEntryException(message: String) : IllegalStateException(message)

class EntityNotFoundException(message: String) : NullPointerException(message)

class DateFormatParseException(message: String, errorOffset: Int): ParseException(message, errorOffset)

class ConstraintsViolationException(message: String) : Exception(message)