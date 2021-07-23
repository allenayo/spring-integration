package com.allenayo.sj.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

    public static String toJson(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    public static <T> T toBean(String xmlString, Class<T> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(xmlString, clazz);
    }

}