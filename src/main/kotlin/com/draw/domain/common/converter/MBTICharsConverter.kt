package com.draw.domain.common.converter

import com.draw.common.converter.GenericJsonConverter
import com.draw.domain.common.enums.MBTIChar
import com.fasterxml.jackson.core.type.TypeReference

class MBTICharsConverter : GenericJsonConverter<List<MBTIChar>>(object : TypeReference<List<MBTIChar>>() {})
