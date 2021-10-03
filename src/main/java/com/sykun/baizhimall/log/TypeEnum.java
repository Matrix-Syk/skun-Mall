package com.sykun.baizhimall.log;

/**
 * @author SYK
 */

public enum TypeEnum {
    INSERT("INSERT","添加"),
    SELECT("SELECT","查询"),
    DELETE("DELETE","删除"),
    UPDATE("UPDATE","更新");

    TypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
