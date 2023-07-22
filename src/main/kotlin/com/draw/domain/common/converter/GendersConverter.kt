package com.draw.domain.common.converter

import com.draw.common.converter.GenericJsonConverter
import com.draw.domain.common.enums.Gender
import com.fasterxml.jackson.core.type.TypeReference

class GendersConverter : GenericJsonConverter<List<Gender>>(object : TypeReference<List<Gender>>() {})
