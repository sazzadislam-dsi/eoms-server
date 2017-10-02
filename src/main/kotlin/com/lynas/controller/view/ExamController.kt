package com.lynas.controller.view

import com.lynas.model.util.ExamType
import com.lynas.service.ClassService
import com.lynas.service.ExamService
import com.lynas.service.SubjectService
import com.lynas.util.getCurrentYear
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by muztaba.hasanat on 2/15/2017.
 */

@Controller
@RequestMapping("exam")
class ExamController constructor(val classService: ClassService,
                                 val subjectService: SubjectService,
                                 val examService: ExamService) {

    val logger = getLogger(this.javaClass)

    @RequestMapping("/class/select")
    fun attendanceBook(model: Model, request: HttpServletRequest): String {
        model.addAttribute("classList",
                classService.findClassListByOrganizationId(getOrganizationFromSession(request).id!!).sortedBy { it.name })
        logger.info("return examClassSelect page")
        return "exmClassSelect"
    }

    @RequestMapping("/studentList/{classId}") // TODO add subject id, cause input result for specific subject
    fun studentListByClass(@PathVariable classId: Long, model: Model, request: HttpServletRequest): String {
        logger.info("hit in studentListByClass with class id {}", classId)
        val organization = getOrganizationFromSession(request)
        val year = getCurrentYear()
        val studentList = classService.findStudentsByClassId(classId, organization.id!!, year)
        val subjectList = subjectService.findAllByClassId(classId, organization.id!!)
        model.addAttribute("studentList", studentList)
        model.addAttribute("subjectList", subjectList)
        model.addAttribute("clsId", classId)
        return "exmStudentList"
    }

    @RequestMapping("/result/update/classId/{classId}/subjectId/{subjectId}/year/{_year}/date/{date}/examType/{examType}")
    fun resultUpdateBySubject(@PathVariable classId: Long,
                              @PathVariable subjectId: Long,
                              @PathVariable _year: Int,
                              @PathVariable date: Date,
                              @PathVariable examType: ExamType,
                              model: Model,
                              request: HttpServletRequest) {
        logger.info("Result update for classId [{}], subjectId [{}], year [{}], date [{}], examType [{}]",
                classId, subjectId, _year, date, examType)

    }

    @RequestMapping("/subject/result")
    fun resultOfSubject(request: HttpServletRequest, model: Model): String {
        logger.info("return subjectResult")
        model.addAttribute("classList",
                classService.findClassListByOrganizationId(getOrganizationFromSession(request).id!!)
                        .sortedBy { it.name })
        return "subjectResult"
    }

    @RequestMapping("/subject/{subjectId}/class/{classId}/result")
    fun resultOfSubjectId(request: HttpServletRequest, model: Model,
                          @PathVariable subjectId: Long, @PathVariable classId: Long): String {
        logger.info("return subjectResult")
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList",
                classService.findClassListByOrganizationId(organization.id!!).sortedBy { it.name })
                .addAttribute("year", getCurrentYear())
                .addAttribute("subject", subjectService.findById(subjectId))
                .addAttribute("cls", classService.findById(classId, organization.id!!))
                // TODO year must be taken from user end
                .addAttribute("examList", examService.examListOfSubject(classId, subjectId, getCurrentYear(), organization.id!!))
        return "subjectResult"
    }

    @RequestMapping("/student/result")
    fun resultOfStudent(request: HttpServletRequest, model: Model): String {
        model.addAttribute("classList",
                classService.findClassListByOrganizationId(getOrganizationFromSession(request).id!!)
                        .sortedBy { it.name })
        // TODO retrieve student by student name not ID
        return "studentResult"
    }

    @RequestMapping("/class/result")
    fun resultOfClass(request: HttpServletRequest, model: Model): String {
        model.addAttribute("classList",
                classService.findClassListByOrganizationId(getOrganizationFromSession(request).id!!)
                        .sortedBy { it.name })

        return "classResult"
    }
}