package cn.cc.novel.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类用于所有返回的情况 code和msg不允许为空
 */
public class ReturnObj {

    /* 状态码 */
    private String code;
    /* 信息 */
    private String value;
    /* Obj */
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setData(int totalNum,int pageNum,Object data) {
        Map<String,Object> map = new HashMap<>();
        map.put("totalNum",totalNum);
        map.put("pageNum",pageNum);
        map.put("obj",data);
        this.data = map;
    }

    public void setStatusEnum(StatusEnum statusEnum){
        this.code = statusEnum.getCode();
        this.value = statusEnum.getValue();
    }

    public ReturnObj(StatusEnum statusEnum, Object data) {
        this.code = statusEnum.getCode();
        this.value = statusEnum.getValue();
        this.data = data;
    }

    public ReturnObj(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public ReturnObj(StatusEnum statusEnum) {
        this.code = statusEnum.getCode();
        this.value = statusEnum.getValue();
    }

    public ReturnObj() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
