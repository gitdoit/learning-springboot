package org.seefly.springwebsocket.converter;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author liujianxin
 * @date 2019-02-25 20:48
 */
public class CustomMessageConverter extends AbstractMessageConverter {
    private static final Base64.Decoder DECODER = Base64.getDecoder();


    public CustomMessageConverter() {
     super(new MimeType("text", "plain", StandardCharsets.UTF_8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return ByteBuffer.class.isAssignableFrom(clazz);
    }


    /**
     * 从外部流入
     */
    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, @Nullable Object conversionHint) {
        // Base64 -> byte
        Object payload = message.getPayload();
        String base64 = (payload instanceof String ? (String) payload : new String((byte[]) payload, StandardCharsets.UTF_8));
        // data:audio/wav;base64,UklGRnktAABXQVZFZ...   去头
        String substring = base64.substring(base64.indexOf(",")+1);
        // 解码
        byte[] decode = DECODER.decode(substring);
        // 封装
        return  ByteBuffer.wrap(decode);
    }

    /**
     * 从服务流出
     */
    @Override
    @Nullable
    protected Object convertToInternal(
            Object payload, @Nullable MessageHeaders headers, @Nullable Object conversionHint) {
        if (byte[].class == getSerializedPayloadClass()) {
            payload = ((String) payload).getBytes(StandardCharsets.UTF_8);
        }
        return payload;
    }
}
