package com.lynas.exception

import org.springframework.dao.DuplicateKeyException

/**
 * Created by seal on 6/27/2017.
 */
class SameDateAttendanceException(string: String) : DuplicateKeyException(string) {

}