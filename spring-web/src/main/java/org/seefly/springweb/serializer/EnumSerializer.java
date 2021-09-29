package org.seefly.springweb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author liujianxin
 * @date 2019/10/25 13:42
 */
public class EnumSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private String fldLetter;
    private  Map<String,String> dictionaryMap;
    public EnumSerializer(){
        this.fldLetter = "";
    }

    public EnumSerializer(String fldLetter, Map<String,String> dictionary) {
        this.fldLetter = fldLetter;
        this.dictionaryMap = dictionary;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String text = dictionaryMap.get(s);
        jsonGenerator.writeString(text);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals (beanProperty.getType ().getRawClass (), String.class)) {
                PlatformEnum platformEnum = beanProperty.getAnnotation (PlatformEnum.class);
                if (platformEnum == null) {
                    platformEnum = beanProperty.getContextAnnotation (PlatformEnum.class);
                }
                if (platformEnum != null) {
                   // List<ComboTreeData> dictionaryByName = service.getDictionaryByName(platformEnum.fldLetter());
                   // Map<String, String> dictionaryMap = dictionaryByName.stream().collect(Collectors.toMap(ComboTreeData::getId, ComboTreeData::getText));
                   // return new EnumSerializer (platformEnum.fldLetter(),dictionaryMap);
                }
            }
            return serializerProvider.findValueSerializer (beanProperty.getType (), beanProperty);
        }
        return serializerProvider.findNullValueSerializer (beanProperty);
    }
}
