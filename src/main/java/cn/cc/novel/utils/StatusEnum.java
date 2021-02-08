package cn.cc.novel.utils;

// 定义返回状态
public enum StatusEnum {

    //调整拦截器的错误和file的错误总结到一起，然后全部重新命名，注释掉，看报错

    Status001("001","出错了"),
    Status200("200","成功");


    private String code;
    private String value;

    StatusEnum() {
    }

    StatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

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
}
