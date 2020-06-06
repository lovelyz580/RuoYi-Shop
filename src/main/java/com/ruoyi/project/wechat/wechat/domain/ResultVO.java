package com.ruoyi.project.wechat.wechat.domain;

import java.util.List;

/**
 * 通用数据模板-带数据List
 * <p>
 * code(默认为0)
 * <p>
 * count(数据的数量)
 * <p>
 * msg(消息:"找到相关数据"，"未找到相关数据"...)
 * <p>
 * data(数据List)
 * <p>
 * 返回实体类
 * <p>
 * Created by Lovelyz on 2019/05/22.
 */

public class ResultVO<T> {

    /**
     * 默认为0
     * 200 查询成功
     * 300 失败
     * 260 暂无数据
     * 400 参数错误
     * 460 分页参数错误
     * 500 服务器错误
     */
    private int code;

    /**
     * 数据的数量
     */
    private int count;

    /**
     * 消息:"找到相关数据"，"未找到相关数据"...
     */
    private String msg;

    /**
     * 单条数据
     */
    private T dataone;

    /**
     * 数据List
     */
    private List<T> data;


    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public T getDataone() {
        return dataone;
    }

    public void setDataone(T dataone) {
        this.dataone = dataone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
