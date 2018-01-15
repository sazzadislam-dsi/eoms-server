package com.lynas.service

import com.lynas.model.FeeInfo
import com.lynas.model.StudentFee
import com.lynas.model.util.FeeInfoOfStudentResponse
import com.lynas.repo.FeeInfoRepository
import org.neo4j.ogm.session.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 5/18/17.
 */


@Service
class FeeInfoService(val feeInfoRepository: FeeInfoRepository, val session: Session) {

    @Transactional
    fun create(feeInfo: FeeInfo): FeeInfo {
        return feeInfoRepository.save(feeInfo)
    }

    @Transactional
    fun findFeeInfoByClass(id: Long): List<FeeInfo> {
        return feeInfoRepository.findFeeInfoByClass(id)
    }

    @Transactional
    fun findStudentFeeInfoByClass(id: Long): List<StudentFee> {
        return feeInfoRepository.findStudentFeeInfoByClass(id)
    }

    @Transactional
    fun findStudentFeeInfoByStudent(id: Long, orgId: Long): List<FeeInfoOfStudentResponse>? {
        return feeInfoRepository
                .findStudentFeeInfoByStudent(id)
                ?.filter { it.feeInfo.course.organization.id == orgId }
                ?.map { FeeInfoOfStudentResponse(
                        id = it.id!!, studentId = it.student.id!!, paymentDate = it.paymentDate, feeInfo = it.feeInfo
                ) }
                ?.toMutableList()
    }

    @Transactional
    fun find(id: Long): FeeInfo? {
        return feeInfoRepository.findOne(id, 2)
    }

    @Transactional
    fun delete(id: Long): Boolean {
        return try {
            feeInfoRepository.delete(id)
            true
        } catch (e: Exception) {
            false
        }
    }

    @Transactional
    fun findFeeInfoByStudent(id: Long): List<FeeInfo> {
        return feeInfoRepository.findFeeInfoByStudent(id = id)
    }

}




















