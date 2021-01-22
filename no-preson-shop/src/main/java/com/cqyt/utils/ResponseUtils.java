package com.cqyt.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Hs
 */
@Data
@NoArgsConstructor
public class ResponseUtils implements Serializable {
    private static int PageNumber = 1;
    private static int PageSize = 10;
    /**
     * 返回的消息
     */
    private String msg;

    /**
     * 返回成功的消息
     */
    private static String successMsg = "操作成功";

    /**
     * 返回错误的消息
     */
    private static String errorMsg = "操作失败";
    /**
     * 返回的状态
     */
    private Integer status = 200;
    /**
     * 返回的结果
     */
    private Boolean res = true;
    /**
     * 返回的错误码
     */
    private Integer errorCode;
    /**
     * 返回的数据
     */
    private Object data;

    /**
     * 返回为成功的构造函数
     *
     * @param msg
     * @param data
     */
    public ResponseUtils(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构造失败的回调结果
     *
     * @param msg
     * @param data
     * @param errorCode
     * @param status
     */
    public ResponseUtils(String msg, Object data, Integer errorCode, Integer status) {
        this.msg = msg;
        this.data = data;
        this.errorCode = errorCode;
        this.status = status;
        this.res = false;
    }

    /**
     * 调用成功的时候，返回成功的状态
     *
     * @param msg
     * @param data
     * @return
     */
    public static ResponseUtils ok(String msg, Object data) {
        return new ResponseUtils(msg, data);
    }

    /**
     * 调用成功的时候，返回成功的状态
     *
     * @return
     */
    public static ResponseUtils get(boolean res) {
        if (res) {
            return ResponseUtils.ok(successMsg);
        } else {
            return ResponseUtils.fail(errorMsg);
        }
    }

    public static ResponseUtils ok(String msg) {
        return new ResponseUtils(msg, null);
    }

    /**
     * 调用失败的时候，返回失败的状态
     *
     * @param msg
     * @param data
     * @return
     */
    public static ResponseUtils fail(String msg, Object data, Integer errorCode, Integer status) {
        return new ResponseUtils(msg, data, errorCode, status);
    }

    /**
     * 调用失败的时候，返回失败的状态
     */
    public static ResponseUtils fail(String msg) {
        return new ResponseUtils(msg, null, null, 200);
    }

    /**
     * 调用失败的时候，返回失败的状态
     *
     * @param
     * @param
     * @return
     */
    public static ResponseUtils fail(String msg, Integer errorCode) {
        return new ResponseUtils(msg, null, errorCode, 200);
    }

    /**
     * 出现异常时候返回对应的code码和错误消息
     *
     * @param msg
     * @param errorCode
     * @param status
     * @return
     */
    public static ResponseUtils fail(String msg, Integer errorCode, Integer status) {
        return new ResponseUtils(msg, null, errorCode, status);
    }
}
