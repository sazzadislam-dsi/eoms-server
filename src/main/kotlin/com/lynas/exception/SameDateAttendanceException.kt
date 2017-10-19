package com.lynas.exception

import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by seal on 6/27/2017.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Same date attendance entry")
class SameDateAttendanceException(string: String) : IllegalStateException(string)