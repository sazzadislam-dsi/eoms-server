package com.lynas.util

import com.lynas.exception.DateFormatParseException
import com.lynas.model.Course
import com.lynas.model.Organization
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletRequest


/**
 * Created by sazzad on 8/17/16
 */

object AppConstant {
    val organization = "ORGANIZATION"
}

fun encodePassword(password: String?): String = BCryptPasswordEncoder().encode(password)

fun getLogger(clazz: Class<*>): Logger {
    return LoggerFactory.getLogger(clazz)
}

fun verifyClassOrganization(cls: Course, request: HttpServletRequest): Boolean {
    if (cls.organization.name.equals((request.session.getAttribute(AppConstant.organization) as Organization).name)) {
        return true
    }
    return false
}

fun responseOK(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.OK)
}

fun responseCreated(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.CREATED)
}

fun responseConflict(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.CONFLICT)
}

fun responseError(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.BAD_REQUEST)
}

fun responseNotFound(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.NOT_FOUND)
}

data class ErrInf(val input: Any, val msg: Any)

@Throws(DateFormatParseException::class)
fun String.convertToDate(): Date {
    val pattern = if (this.matches(kotlin.text.Regex(
                    "[A-Za-z]+ [A-Za-z]+ \\d?\\d \\d{1,2}:\\d{1,2}:\\d{1,2} [A-Za-z]{3} \\d{4}"))) {
        "EEE MMM dd HH:mm:ss Z yyyy"
    } else {
        "dd-MM-yyyy"
    }
    val dateFormatter = SimpleDateFormat(pattern)
    return try {
        dateFormatter.parse(this)
    } catch (e: ParseException) {
        throw DateFormatParseException("Invalid date format [${this}], " +
                "Expected date format ${Constants.EXPECTED_DATE_FORMAT}", e.errorOffset)
    }
}

fun Date.convertToString() = SimpleDateFormat("dd-MM-yyyy").format(this)

fun getCurrentYear() = LocalDate.now().year


fun String.err_notFound() = "Object or Property not found with given input : ${this}"
