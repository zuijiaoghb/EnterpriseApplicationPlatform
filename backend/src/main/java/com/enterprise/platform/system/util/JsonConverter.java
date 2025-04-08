package com.enterprise.platform.system.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Converter
public class JsonConverter implements AttributeConverter<String, String> {
    
    private static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return attribute != null ? mapper.writeValueAsString(attribute) : "[]";
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? mapper.readValue(dbData, String.class) : "[]";
        } catch (Exception e) {
            return "[]";
        }
    }
}