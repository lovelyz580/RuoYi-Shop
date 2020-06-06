package com.ruoyi.project.wechat.wechat.domain;

import java.util.Date;
import java.util.List;

public class WechatForm {
    /**
     * 序号(自增主键)
     */
    private Integer id;

    /**
     * 小程序的formid
     */
    private String formid;

    /**
     * 用户的id
     */
    private String userid;

    /**
     * 小程序中需要发送的openid
     */
    private String openid;

    /**
     * 小程序发送消息的模板ID
     */
    private String templateid;

    /**
     * formid的创建时间
     */
    private Date createdate;

    /**
     * 预留字段1
     */
    private String wechatother1;

    /**
     * 预留字段2
     */
    private String wechatother2;

    /**
     * 预留字段3
     */
    private String wechatother3;

    // 版本信息
    /**
     * 版本号
     */
    private String version;

    // 分页数据
    /**
     * 当前页数(如果不进行分页，该条数据默认为-1)
     */
    private Integer pagenumber;

    /**
     * 每页的数据量
     */
    private Integer pagesize;

    // 操作数据
    /**
     * 根据idlist删除信息时使用
     */
    private List<Integer> idlist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid == null ? null : formid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid == null ? null : templateid.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getWechatother1() {
        return wechatother1;
    }

    public void setWechatother1(String wechatother1) {
        this.wechatother1 = wechatother1 == null ? null : wechatother1.trim();
    }

    public String getWechatother2() {
        return wechatother2;
    }

    public void setWechatother2(String wechatother2) {
        this.wechatother2 = wechatother2 == null ? null : wechatother2.trim();
    }

    public String getWechatother3() {
        return wechatother3;
    }

    public void setWechatother3(String wechatother3) {
        this.wechatother3 = wechatother3 == null ? null : wechatother3.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(Integer pagenumber) {
        this.pagenumber = pagenumber;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public List<Integer> getIdlist() {
        return idlist;
    }

    public void setIdlist(List<Integer> idlist) {
        this.idlist = idlist;
    }
}