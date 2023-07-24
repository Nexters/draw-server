package com.draw.domain.feed

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.draw.domain.common.BaseEntity
import com.draw.domain.common.converter.GendersConverter
import com.draw.domain.common.converter.MBTICharsConverter
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
    var content: String? = null

    @Column(nullable = false)
    var writerId: Long? = null

    @Column(nullable = false)
    @Convert(converter = GendersConverter::class)
    var genders: MutableList<Gender> = mutableListOf()

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var ageRange: AgeRange? = null

    @Column(nullable = false)
    @Convert(converter = MBTICharsConverter::class)
    var mbtiChars: MutableList<MBTIChar> = mutableListOf()

    @Formula("(select count(*) from favorite_feed ff where ff.feed_id = id)")
    var favoriteCount: Int? = null
}