package com.draw.domain.feed

import com.draw.domain.common.BaseEntity
import com.draw.domain.common.converter.GendersConverter
import com.draw.domain.common.converter.MBTICharsConverter
import com.draw.domain.common.enums.AgeRange
import com.draw.domain.common.enums.Gender
import com.draw.domain.common.enums.MBTIChar
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.Formula

@Entity
class Feed : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var userId: Long? = null

    @Column(nullable = false)
    var content: String? = null

    @Convert(converter = GendersConverter::class)
    var genders: MutableList<Gender> = mutableListOf()

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var ageRange: AgeRange? = null

    @Column(nullable = false)
    @Convert(converter = MBTICharsConverter::class)
    var mbtiChars: MutableList<MBTIChar> = mutableListOf()

    @Formula("(select count(*) from favorite_feed ff where ff.feed_id = id)")
    var favoriteCount: Int? = null
}
