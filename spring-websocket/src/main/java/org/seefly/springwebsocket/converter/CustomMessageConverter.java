package org.seefly.springwebsocket.converter;

import org.seefly.springwebsocket.model.SessionData;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author liujianxin
 * @date 2019-02-25 20:48
 */
public class CustomMessageConverter extends AbstractMessageConverter {
    protected CustomMessageConverter(MimeType supportedMimeType) {
     super(new MimeType("text", "plain", StandardCharsets.UTF_8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return SessionData.class.isAssignableFrom(clazz);
    }


    /**
     * 从外部流入
     */
    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, @Nullable Object conversionHint) {
        Object payload = message.getPayload();
        return (payload instanceof String ? payload : new String((byte[]) payload, StandardCharsets.UTF_8));
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
