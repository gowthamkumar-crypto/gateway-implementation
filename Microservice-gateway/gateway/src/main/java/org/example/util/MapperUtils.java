package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperUtils {

    ObjectMapper objectMapper = new ObjectMapper();
    public <T> T objectMapper(Object o,Class<T> convertionClassType){
        return objectMapper.convertValue(o,convertionClassType);
    }
}
