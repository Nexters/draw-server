package com.draw.service

import com.draw.infra.persistence.ReplyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReplyService(
    private val replyRepository: ReplyRepository,
) {

}
