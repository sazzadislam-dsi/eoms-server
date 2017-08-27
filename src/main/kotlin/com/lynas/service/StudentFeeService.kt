package com.lynas.service

import com.lynas.model.StudentFee
import com.lynas.repo.StudentFeeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 5/18/17.
 */


@Service
class StudentFeeService(val studentFeeRepository: StudentFeeRepository) {

    @Transactional
    fun create(studentFee: StudentFee): StudentFee {
        return studentFeeRepository.save(studentFee)
    }

}