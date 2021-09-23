package org.seefly.springweb.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

/**
 * @author liujianxin
 * @date 2020/7/2 9:41
 */
public class JacksonUtil {
    private static final ObjectMapper MAPPER;
    static {
        MAPPER = new Jackson2ObjectMapperBuilder()
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .timeZone(TimeZone.getTimeZone("GMT+8"))
                .build();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }


    /**
     * 获取json字符串中的key集合
     *
     * @param jsonData json数据
     * @return key集合
     */
    public static Iterator<String> keys(String jsonData) {
        if (jsonData == null) {
            return null;
        }
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            return jsonNode.fieldNames();
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }


    /**
     * 对象转json
     *
     * @param data javaBean
     * @return json数据
     */
    public static String objectToJson(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    /**
     * {eg:[xx:xx,xx:xx],other:xx,foo:{abc:{xx:xx}}}
     * <p>
     * e.g.
     * String other = JacksonUtil.getValue(jsonData, "other");
     * 获取json中指定key的字符串值
     *
     * @param jsonData json数据
     * @param key      键
     * @return 值
     */
    public static String getValue(String jsonData, String key) {
        if (jsonData == null) {
            return null;
        }
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode value = jsonNode.get(key);
            if (value == null) {
                return null;
            }
            return value.asText();
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    /**
     * {eg:[xx:xx,xx:xx],other:xx,foo:{abc:{xx:xx}}}
     * <p>
     * e.g.
     * String abc = JacksonUtil.getValue(jsonData, jsonNode -> jsonNode.get("foo").get("abc"));
     * 获取json字符串中指定键的值
     *
     * @param jsonData json数据
     * @param function 自定义如何获取该json字符串中的指定key的值,例如嵌套的json，想获取内部第n层的某个key，可以自己选择
     * @return 值
     */
    public static String getValue(String jsonData, Function<JsonNode, JsonNode> function) {
        if (jsonData == null) {
            return null;
        }
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode apply = function.apply(jsonNode);
            if (apply == null) {
                return null;
            }
            return apply.asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {eg:[xx:xx,xx:xx],other:xx,foo:{abc:{xx:xx}}}
     * <p>
     * e.g.
     * Object abc = JacksonUtil.getValue(jsonData, jsonNode -> jsonNode.get("foo").get("abc"), Object.class);
     * 获取json数据中指定key的值，并转换成目标对象
     *
     * @param jsonData json数据
     * @param function 如何获取该json字符串中的值，例如嵌套的json，想获取内部n层的某个key，可以自己选择
     * @param beanType 目标类型
     * @return 转换后数据
     */
    public static <T> T getValue(String jsonData, Function<JsonNode, JsonNode> function, Class<T> beanType) {
        if (jsonData == null) {
            return null;
        }
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode apply = function.apply(jsonNode);
            if (apply == null) {
                return null;
            }
            return MAPPER.treeToValue(apply, beanType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {eg:[xx:xx,xx:xx],other:{xx:xx,xx:xx},foo:{abc:{xx:xx}}}
     * <p>
     * e.g.
     * Object other = JacksonUtil.getValue(jsonData, "other", Object.class);
     * 获取json数据中指定key的值，并将其转换成目标对象
     *
     * @param jsonData json数据
     * @param key      键
     * @param beanType 目标类型
     * @return 数据
     */
    public static <T> T getValue(String jsonData, String key, Class<T> beanType) {
        if (jsonData == null) {
            return null;
        }
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData).get(key);
            if (jsonNode == null) {
                return null;
            }
            return MAPPER.treeToValue(jsonNode, beanType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 普通json转对象
     *
     * @param jsonData json数据
     * @param beanType 目标类型
     * @return 目标数据
     */
    public static <T> T jsonToBean(String jsonData, Class<T> beanType) {
        if (jsonData == null || beanType == null) {
            return null;
        }
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转成带泛型的bean,例如 HttpResult<Foo> 这种形式的
     * <br/>
     * e.g.
     * <br/>
     * HttpResult<Foo> result = JacksonUtil.jsonToParametricTypeBean(jsonData,HttpResult.class,Foo.class);
     *
     * @param jsonData json数据
     * @param clazz    类型
     * @param generic  泛型
     */
    public static <T> T jsonToParametricTypeBean(String jsonData, Class<T> clazz, Class<?> generic) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(clazz, generic);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {eg:[xx:xx,xx:xx],other:xx,foo:{xx:xx}}
     * <p>
     * e.g.
     * List<Object> bean = JacksonUtil.jsonToList(jsonData, Object.class, jsonNode -> jsonNode.withArray("eg"));
     * <p>
     * <p>
     * json数据转列表
     *
     * @param jsonData json数据
     * @param beanType 目标类型
     * @param function 如何获取该json数据中的json数组，例如嵌套的json，某个key为数组形式，可以用该方法
     * @return 数组
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType, Function<JsonNode, ArrayNode> function) {
        if (jsonData == null || beanType == null || function == null) {
            return null;
        }
        try {
            ArrayNode arrayNode = function.apply(MAPPER.readTree(jsonData));
            if (arrayNode == null) {
                return null;
            }
            if (arrayNode.size() > 0) {
                List<T> list = new ArrayList<>(arrayNode.size());
                for (JsonNode jsonNode : arrayNode) {
                    T t = MAPPER.treeToValue(jsonNode, beanType);
                    list.add(t);
                }
                return list;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * json数组转列表
     *
     * @param jsonData json数组字符串
     * @param beanType 目标类型
     * @return 列表
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        if (jsonData == null || beanType == null) {
            return null;
        }
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json转Map
     *
     * @param jsonData  json数据
     * @param keyType   key类型
     * @param valueType value类型
     * @return map
     */
    public static <K, V> Map<K, V> jsonToMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        if (jsonData == null || keyType == null || valueType == null) {
            return null;
        }
        JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
