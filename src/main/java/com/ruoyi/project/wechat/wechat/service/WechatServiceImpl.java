package com.ruoyi.project.wechat.wechat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.wechat.wechat.mapper.WechatMapper;
import com.ruoyi.project.wechat.wechat.domain.Wechat;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import com.ruoyi.common.support.Convert;

/**
 * 微信相关数据 服务层实现
 * 
 * @author lovelyz
 * @date 2020-06-06
 */
@Service
public class WechatServiceImpl implements IWechatService 
{
	@Autowired
	private WechatMapper wechatMapper;

	/**
     * 查询微信相关数据信息
     * 
     * @param iD 微信相关数据ID
     * @return 微信相关数据信息
     */
    @Override
	public Wechat selectWechatById(Integer iD)
	{
	    return wechatMapper.selectWechatById(iD);
	}
	
	/**
     * 查询微信相关数据列表
     * 
     * @param wechat 微信相关数据信息
     * @return 微信相关数据集合
     */
	@Override
	public List<Wechat> selectWechatList(Wechat wechat)
	{
	    return wechatMapper.selectWechatList(wechat);
	}
	
    /**
     * 新增微信相关数据
     * 
     * @param wechat 微信相关数据信息
     * @return 结果
     */
	@Override
	public int insertWechat(Wechat wechat)
	{
	    return wechatMapper.insertWechat(wechat);
	}
	
	/**
     * 修改微信相关数据
     * 
     * @param wechat 微信相关数据信息
     * @return 结果
     */
	@Override
	public int updateWechat(Wechat wechat)
	{
	    return wechatMapper.updateWechat(wechat);
	}

	/**
     * 删除微信相关数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteWechatByIds(String ids)
	{
		return wechatMapper.deleteWechatByIds(Convert.toStrArray(ids));
	}
	
}
