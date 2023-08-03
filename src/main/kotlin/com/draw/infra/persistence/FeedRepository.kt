package com.draw.infra.persistence

import com.draw.domain.feed.Feed
import org.springframework.data.jpa.repository.JpaRepository

interface FeedRepository : JpaRepository<Feed, Long>, FeedRepositorySupport
