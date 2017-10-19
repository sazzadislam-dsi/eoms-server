package com.lynas.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by seal on 6/4/2017.
 */

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate course info")
class DuplicateCourseException(string: String) : IllegalStateException(string)