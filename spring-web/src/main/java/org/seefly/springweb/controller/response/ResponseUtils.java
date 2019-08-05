package org.seefly.springweb.controller.response;

import java.util.HashMap;
import java.util.Map;

/**
 * web响应结果工具类
 *
 */
public abstract class ResponseUtils {

    private ResponseUtils() {
    }

    /**
     * 返回成功响应.
     */
    public static Response success() {
        return success(null);
    }

    /**
     * 返回成功响应.
     *
     * @param data 数据
     * @return 响应对象
     */
    public static <T> Response<T> success(T data) {
        return new CamelResponse<>(data);
    }

    /**
     * 返回成功响应.
     *
     * @param dataName 数据对象名称
     * @param data     数据对象
     * @return 响应对象
     */
    public static Response<Map<String, Object>> success(String dataName, Object data) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(dataName, data);
        return new CamelResponse<>(map);
    }

    /**
     * 返回错误的响应.
     *
     * @param errorCode 错误编码
     * @param errorMsg  错误信息
     * @return 响应对象
     */
    public static Response error(Integer errorCode, String errorMsg) {
        return new CamelResponse(errorCode, errorMsg);
    }

    /**
     * 返回错误的响应.
     *
     * @param errorCode 错误编码
     * @param errorMsg  错误信息
     * @param throwable 异常对象
     * @return 响应对象
     */
    public static Response error(Integer errorCode, String errorMsg, Throwable throwable) {
        return new CamelResponse(errorCode, errorMsg, throwable);
    }
}