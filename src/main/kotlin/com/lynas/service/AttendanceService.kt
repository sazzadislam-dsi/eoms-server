package com.lynas.service

import com.lynas.exception.DuplicateEntryException
import com.lynas.exception.EntityNotFoundForGivenIdException
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
        val course = classService.findById(attendanceJsonWrapper.classId, orgId)
                ?: throw EntityNotFoundForGivenIdException(
                "Course or class not found with given classID : ${attendanceJsonWrapper.classId}")
        val foundDuplicate = checkExistingAttendanceOnGivenDate(_attendanceDate, attendanceJsonWrapper.classId, orgId)
        if (foundDuplicate) {
            throw DuplicateEntryException("Attendance duplicate entry found at date $_attendanceDate")
        }

        //TODO catch this exception properly
        val set = attendanceJsonWrapper.attendanceJson.map {
            StudentAttendance(
                    student = studentService.findById(it.t, orgId)
                            ?: throw EntityNotFoundForGivenIdException("student Not found with given studentId: ${it.t}"),
                    attendanceStatus = it.i)
        }.toMutableSet()

        val attendanceBook = AttendanceBook(studentAttendances = set, attendanceDate = _attendanceDate, course = course)
        return attendanceRepository.save(attendanceBook)
    }

    private fun checkExistingAttendanceOnGivenDate(_attendanceDate: Date, classId: Long, orgId: Long): Boolean {
        return attendanceRepository.findAttendanceBookOfClass(_attendanceDate.time, classId, orgId)
                .isEmpty()
                .not()
    }


    @Transactional
    fun getAttendanceOfAClassOnDate(date: Long, classId: Long, orgId: Long): List<AttendanceViewQueryResult> {
        return attendanceRepository.findAttendanceBookOfClass(date, classId, orgId)
    }


}