package com.draw.infra.persistence

import com.draw.domain.reply.Reply
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<Reply, Long>, ReplyRepositorySupport
