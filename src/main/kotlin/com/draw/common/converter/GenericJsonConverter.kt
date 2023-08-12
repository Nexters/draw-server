package com.draw.common.converter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import mu.KotlinLogging
import java.io.IOException

open class GenericJsonConverter<T>(private val typeReference: TypeReference<T>) : AttributeConverter<T, String> {
    private val log = KotlinLogging.logger { }
    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(additionalData: T): String {
        // Object -> Json 문자열
        return try {
            objectMapper.writeValueAsString(additionalData)
        } catch (e: JsonProcessingException) {
            log.error("fail to serialize as object into Json : {}", additionalData, e)
            throw RuntimeException(e)
        }
    }

    override fun convertToEntityAttribute(jsonStr: String?): T? {
        // Json 문자열 -> Object

        // TODO (larry.x) writerInfo 가 null 인 경우에 jsonStr not-nullable 이어서
        // 실패하는 문제가 있어서 임시로 처리해두었음
        if (jsonStr == null) {
            return null
        }
        return try {
            objectMapper.readValue(jsonStr, typeReference)
        } catch (e: IOException) {
            log.error("fail to deserialize as Json into Object : {}", jsonStr, e)
            throw RuntimeException(e)
        }
    }
}
