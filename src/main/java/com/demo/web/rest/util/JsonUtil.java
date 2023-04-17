package com.demo.web.rest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonUtil() {
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                String result = mapper.writeValueAsString(obj);
                return result;
            } catch (JsonProcessingException var3) {
                log.error("toJson error", var3);
                return null;
            }
        }
    }

    public static <T> T json2Obj(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        } else {
            try {
                T result = mapper.readValue(jsonStr, clazz);
                return result;
            } catch (Exception var3) {
                log.error("json2Obj error", var3);
                return null;
            }
        }
    }

    public static <T> List<T> json2List(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

            try {
                return (List) mapper.readValue(json, javaType);
            } catch (IOException var4) {
                log.error("json2Obj error", var4);
                return null;
            }
        }
    }

    public static <V> Map<String, V> json2Map(String json, Class<V> v) {
        return json2Map(json, String.class, v);
    }

    public static <K, V> Map<K, V> json2Map(String json, Class<K> k, Class<V> v) {
        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, new Class[]{k, v});

            try {
                return (Map) mapper.readValue(json, javaType);
            } catch (IOException var5) {
                log.error("json2Obj error", var5);
                return null;
            }
        }
    }

    public static <T> T json2Obj(TreeNode jsonNode, Class<T> clazz) {
        if (jsonNode == null) {
            return null;
        } else {
            try {
                T result = mapper.treeToValue(jsonNode, clazz);
                return result;
            } catch (Exception var3) {
                log.error("json2Obj error", var3);
                return null;
            }
        }
    }

    public static <T extends JsonNode> T convertObj(Object obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                T result = mapper.valueToTree(obj);
                return result;
            } catch (Exception var2) {
                log.error("convertObj error", var2);
                return null;
            }
        }
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
