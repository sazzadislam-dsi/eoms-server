package com.lynas.service

import com.lynas.model.FeeInfo
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
    open fun save(feeInfo: FeeInfo): FeeInfo {
        feeInfoRepository.findAll()
        return feeInfoRepository.save(feeInfo)
    }

    @Transactional
    open fun findFeeInfoByClass(id: Long): List<FeeInfo>? {
        return feeInfoRepository.findFeeInfoByClass(id)
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
        return feeInfoRepository.findFeeInfoByStudent(id = id)
    }

}




















