/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aditya
 *
 */
public class ExpectationUtils {

    public static Map<String, Object> getJsonAsMap(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
            };
            HashMap<String, Object> result = mapper.readValue(json, typeRef);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Couldnt parse json:" + json, e);
        }
    }

    public static List<Map<String, Object>> getJsonAsList(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {
            };
            List<Map<String, Object>> result = mapper.readValue(json, typeRef);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Couldnt parse json:" + json, e);
        }
    }

}
