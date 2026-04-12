package com.example.demo.r;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.apache.http.HttpStatus;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
    public R setData(Object data) {
        put("data", data);
        return this;
    }

    public <T> T getData(TypeReference<T> typeReference) {
        Object data = get("data");
        String jsonString = JSON.toJSONString(data);
        T t = JSON.parseObject(jsonString, typeReference);
        return t;
    }

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        R r = new R();
        r.put("code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        r.put("msg", "系统异常");
        return r;
    }

    public static R error(String msg) {
        R r = new R();
        r.put("code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        r.put("msg", msg);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

}
