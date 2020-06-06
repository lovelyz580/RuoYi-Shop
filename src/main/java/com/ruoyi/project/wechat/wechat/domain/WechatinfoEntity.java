package com.ruoyi.project.wechat.wechat.domain;

/**
 * Created by Lovelyz
 * on 2019-06-13 09:40
 */
public class WechatinfoEntity {

//   用户国家
    private String country;
//    用户性别
    private Integer gender;
//    用户国家
    private String province;
//    用户城市
    private String city;
//    用户头像
    private String avatarUrl;
//    用户openid
    private String openId;
//    用户名称
    private String nickName;
//    用户语言
    private String language;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}


