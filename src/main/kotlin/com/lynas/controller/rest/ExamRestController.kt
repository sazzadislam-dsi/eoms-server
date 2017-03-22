package com.lynas.controller.rest

import com.lynas.model.Exam
import com.lynas.service.ExamService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("exams")
class ExamRestController(val examService: ExamService) {

    @PostMapping
    fun post(@RequestBody exam: Exam): Exam {
        examService.save(exam)
        return exam
    }
}