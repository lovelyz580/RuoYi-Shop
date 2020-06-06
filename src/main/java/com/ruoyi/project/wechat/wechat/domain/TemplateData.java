package com.ruoyi.project.wechat.wechat.domain;

/**
 *服务通知
 * Created by Lovelyz on 2019/05/22.
 */

public class TemplateData {

    private String key;
    private String value;

    private String color;

    public TemplateData() { super(); } public TemplateData(String value, String color) { super(); this.value = value; this.color = color; } public String getValue() { return value; } public void setValue(String value) { this.value = value; } public String getColor() { return color; } public void setColor(String color) { this.color = color; }
}