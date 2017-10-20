package com.lynas.service

import com.lynas.exception.SameDateAttendanceException
import com.lynas.model.AttendanceBook
import com.lynas.model.StudentAttendance
import com.lynas.model.util.AttendanceJsonWrapper
import com.lynas.model.util.AttendanceViewQueryResult
import com.lynas.repo.AttendanceRepository
import com.lynas.util.convertToDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by seal on 2/8/2017
 */
@Service
class AttendanceService constructor(val studentService: StudentService,
                                    val classService: ClassService,
                                    val attendanceRepository: AttendanceRepository) {

    @Transactional
    fun create(attendanceJsonWrapper: AttendanceJsonWrapper, orgId: Long): AttendanceBook {
        //TODO what happens if user gives invalid date format
        val _attendanceDate = attendanceJsonWrapper.date.convertToDate()
        checkExistingAttendanceOnGivenDate(
                _attendanceDate = _attendanceDate,
                classId = attendanceJsonWrapper.classId,
                orgId = orgId)

        //TODO catch this exception properly
        val set = attendanceJsonWrapper.attendanceJson.map {
            StudentAttendance(
                    student = studentService.findById(it.t, orgId)
                            ?: throw NullPointerException("student Not found with given studentId: ${it.t}"),
                    attendanceStatus = it.i)
        }.toMutableSet()

        val attendanceBook = AttendanceBook(
                studentAttendances = set,
                attendanceDate = _attendanceDate,
            course = classService.findById(attendanceJsonWrapper.classId, orgId)
                    ?: throw NullPointerException(
                    "Course or class not found with given classID : ${attendanceJsonWrapper.classId}")
        )
        return attendanceRepository.save(attendanceBook)
    }

    private fun checkExistingAttendanceOnGivenDate(_attendanceDate: Date, classId: Long, orgId: Long) {
        val foundDuplicate = attendanceRepository
                .findAttendanceBookOfClass(_attendanceDate.time, classId, orgId)
                .isEmpty()
                .not()
        if (foundDuplicate) {
            throw SameDateAttendanceException("Attendance duplicate entry found")
        }
    }


    @Transactional
    fun getAttendanceOfAClassOnDate(date: Long, classId: Long, orgId: Long): List<AttendanceViewQueryResult> {
        return attendanceRepository.findAttendanceBookOfClass(date, classId, orgId)
    }


}