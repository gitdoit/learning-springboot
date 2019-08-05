package org.seefly.springweb.controller.response;

/**
 * json响应消息对象
 *
 */
public interface Response<T> {

    Integer getCode();

    String getMessage();

    T getData();

    Object getData(String key);

    void putData(String key, Object value);
}
