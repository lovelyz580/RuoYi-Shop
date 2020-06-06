package com.ruoyi.project.wechat.wechat.mapper;

import com.ruoyi.project.wechat.wechat.domain.Wechat;
import java.util.List;	

/**
 * 微信相关数据 数据层
 * 
 * @author lovelyz
 * @date 2020-06-06
 */
public interface WechatMapper 
{
	/**
     * 查询微信相关数据信息
     * 
     * @param iD 微信相关数据ID
     * @return 微信相关数据信息
     */
	public Wechat selectWechatById(Integer iD);
	
	/**
     * 查询微信相关数据列表
     * 
     * @param wechat 微信相关数据信息
     * @return 微信相关数据集合
     */
	public List<Wechat> selectWechatList(Wechat wechat);
	
	/**
     * 新增微信相关数据
     * 
     * @param wechat 微信相关数据信息
     * @return 结果
     */
	public int insertWechat(Wechat wechat);
	
	/**
     * 修改微信相关数据
     * 
     * @param wechat 微信相关数据信息
     * @return 结果
     */
	public int updateWechat(Wechat wechat);
	
	/**
     * 删除微信相关数据
     * 
     * @param iD 微信相关数据ID
     * @return 结果
     */
	public int deleteWechatById(Integer iD);
	
	/**
     * 批量删除微信相关数据
     * 
     * @param iDs 需要删除的数据ID
     * @return 结果
     */
	public int deleteWechatByIds(String[] iDs);
	
}