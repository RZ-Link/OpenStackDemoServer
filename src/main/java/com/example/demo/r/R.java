package com.example.demo.r;

import lombok.Data;
import org.apache.http.HttpStatus;

@Data
public class R<T> {

    private T data;
    private Integer code;
    private String msg;

    public R() {
        this.code = 0;
        this.msg = "success";
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        r.msg = "系统异常";
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> ok() {
        return new R<>();
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.data = data;
        return r;
    }

}