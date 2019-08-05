package org.seefly.springweb.controller.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础的响应消息实现
 *
 */
@Getter
@Setter
@EqualsAndHashCode
public class CamelResponse<T> implements Response {
    private static final String SUCCESS_STRING = "操作成功！";
    /**
     * 响应码，当success为false时，code值代表服务端错误状态
     */
    protected Integer code = 200;

    /**
     * 响应的消息，当success为false时，message值为服务端的错误信息内容
     */
    protected String message = SUCCESS_STRING;

    /**
     * 响应的数据内容，success为false，该字段不会有值
     */
    protected T data;

    /**
     * 异常信息
     */
    protected String at;

    /**
     * 默认不成功的构造方法
     */
    CamelResponse() {
        this(true);
    }

    /**
     * 响应成功的数据结果
     *
     * @param data 要响应的数据
     */
    CamelResponse(T data) {
        this(200, SUCCESS_STRING, data);
    }

    /**
     * 初始化状态的构造方法
     *
     * @param success 是否成功
     */
    CamelResponse(boolean success) {
        this(success ? 200 : 500, success ? SUCCESS_STRING : "操作失败！", (T) null);
    }

    /**
     * 构造一个响应错误的结果
     *
     * @param code    错误码
     * @param message 错误信息
     */
    CamelResponse(Integer code, String message) {
        this(code, message, (T) null);
    }

    CamelResponse(Integer code, String message, Throwable throwable) {
        this.code = code;
        this.message = message;
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            pw.flush();
            this.at = sw.toString();
        } catch (IOException ignore) {
            // ignore exception
        }
    }

    /**
     * 初始化构造方法
     *
     * @param code    状态码
     * @param message 响应消息
     * @param data    响应的数据对象
     */
    CamelResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public Object getData(String dataName) {
        return this.data instanceof Map ? ((Map) this.data).get(dataName) : null;
    }

    @Override
    public void putData(String dataName, Object dataObj) {
        if (this.data == null) {
            this.data = (T) new HashMap(10);
        }

        if (!(this.data instanceof Map)) {
            throw new IllegalArgumentException();
        }

        ((Map) this.data).put(dataName, dataObj);
    }
}