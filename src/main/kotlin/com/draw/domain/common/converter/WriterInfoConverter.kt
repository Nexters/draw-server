package com.draw.domain.common.converter

import com.draw.common.converter.GenericJsonConverter
import com.draw.domain.reply.WriterInfo
import com.fasterxml.jackson.core.type.TypeReference

class WriterInfoConverter : GenericJsonConverter<WriterInfo>(object : TypeReference<WriterInfo>() {})
