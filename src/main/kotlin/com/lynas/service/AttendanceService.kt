package com.lynas.service

import com.lynas.model.AttendanceBook
import com.lynas.model.StudentAttendance
import com.lynas.model.query.result.AttendanceViewQueryResult
import com.lynas.model.request.AttendanceJsonWrapper
import com.lynas.repo.AttendanceRepository
import com.lynas.util.convertToDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by seal on 2/8/2017
 */
@Service
open class AttendanceService constructor(val studentService: StudentService,
                                         val classService: ClassService,
                                         val attendanceRepository: AttendanceRepository) {

    @Transactional
    open fun post(attendanceJsonWrapper: AttendanceJsonWrapper): AttendanceBook {
        val set = attendanceJsonWrapper.attendanceJson.map {
            i ->
            StudentAttendance().apply {
                student = studentService.findById(i.t)
                attendanceStatus = i.i
            }
        }.toMutableSet()

        val attendanceBook = AttendanceBook().apply {
            studentAttendances = set
            attendanceDate = attendanceJsonWrapper.date.convertToDate()
            course = classService.findById(attendanceJsonWrapper.classId)
        }
        return attendanceRepository.save(attendanceBook)
    }


    @Transactional
    open fun getAttendanceOfAClassOnDate(date: Long, className: String, organizationName: String?): List<AttendanceViewQueryResult> {
        return attendanceRepository.findAttendanceBookOfClass(date, className, organizationName)
    }


}