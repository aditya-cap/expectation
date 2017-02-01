/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.event.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aditya
 *
 */
@Component
@SuppressWarnings("rawtypes")
public class ModelResolver {

    private static Map<String, Class> modelMapping = new HashMap<>();
    private static Map<Integer, Class> modelTypeMapping = new HashMap<>();

    private static final String OBJECT_ID = "ObjectId";
    private static final String OBJECT_TYPE = "ObjectType";

    static {
        modelMapping.put("Order", OrderModel.class);
        modelMapping.put("Product", ProductModel.class);

        modelTypeMapping.put(1, EmailModel.class);
        modelTypeMapping.put(1, SmsModel.class);
    }

    @SuppressWarnings("unchecked")
    public BaseModel resolveModel(Object eventData) {
        Map<String, Object> eventDataMap;
        if (eventData instanceof String) {
            eventDataMap = getJsonAsMap((String) eventData);
        } else {
            throw new RuntimeException("GOD ONLY KNOWS WHAT THIS IS");
        }
        Class clazz = null;
        Object objectType = eventDataMap.get(OBJECT_TYPE);
        if (objectType instanceof String) {
            clazz = modelMapping.get((String) objectType);
        } else if (objectType instanceof Integer) {
            clazz = modelTypeMapping.get((Integer) objectType);
        } else {
            throw new RuntimeException("noone knows what this type is");
        }

        String objectIdStr = (String) eventDataMap.get(OBJECT_ID);
        if (clazz.equals(OrderModel.class)) {
            OrderModel orderModel = new OrderModel(Long.parseLong(objectIdStr));
            //TODO - put restricted set
            orderModel.getValueMap().putAll(eventDataMap);

            List<Map<String, Object>> arguments = (List) eventDataMap.get("DataBusArguments");
            String status = (String) (arguments
                    .stream()
                    .filter((e) -> "OrderStatus".equals(((Map) e).get("CN")))
                    .map((e) -> e.get("NV"))
                    .findFirst()
                    .get());
            orderModel.getValueMap().put("status", status);
            return orderModel;
        } else if (clazz.equals(ProductModel.class)) {
            ProductModel model = new ProductModel(Long.parseLong(objectIdStr));
            model.getValueMap().putAll(eventDataMap);
            model.getValueMap().put("status", eventDataMap.get("Action"));
            return model;
        } else if (clazz.equals(SmsModel.class)) {
            SmsModel model = new SmsModel(Long.parseLong(objectIdStr));
            model.getValueMap().putAll(eventDataMap);
            model.getValueMap().put("status", eventDataMap.get("EventStatus"));
            return model;
        } else if (clazz.equals(EmailModel.class)) {
            EmailModel emailModel = new EmailModel(Long.parseLong(objectIdStr));
            emailModel.getValueMap().putAll(eventDataMap);
            emailModel.getValueMap().put("status", eventDataMap.get("EventStatus"));
            return emailModel;

        }

        return null;
    }

    private Map<String, Object> getJsonAsMap(String json) {
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
}
