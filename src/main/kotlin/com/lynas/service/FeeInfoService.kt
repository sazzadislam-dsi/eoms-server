package com.lynas.service

import com.lynas.model.FeeInfo
import com.lynas.repo.FeeInfoRepository
import org.neo4j.ogm.session.Session
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 5/18/17.
 */


@Service
class FeeInfoService(val feeInfoRepository: FeeInfoRepository, val session: Session) {

    @Transactional
    open fun save(feeInfo: FeeInfo): FeeInfo {
        feeInfoRepository.findAll()
        return feeInfoRepository.save(feeInfo)
    }

    @Transactional
    open fun findAll(): MutableIterable<FeeInfo>? {
        val page = PageRequest(0, 100, Sort(Sort.Direction.DESC, "dateCreated"))
        return feeInfoRepository.findAll(page, 2)
    }

    @Transactional
    open fun find(id: Long): FeeInfo? {
        return feeInfoRepository.findOne(id, 2)
    }

    @Transactional
    open fun delete(id: Long): Boolean {
        try {
            feeInfoRepository.delete(id)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    @Transactional
    open fun findFeeInfoByStudent(id: Long): List<FeeInfo>? {
        return feeInfoRepository.findFeeInfo(studentId = id)
    }

}




















