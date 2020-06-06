package com.ruoyi.project.wechat.wechat.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.wechat.wechat.domain.Wechat;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 微信相关数据 信息操作处理
 * 
 * @author lovelyz
 * @date 2020-06-06
 */
@Controller
@RequestMapping("/wechat/wechat")
public class WechatController extends BaseController
{
    private String prefix = "wechat/wechat";
	
	@Autowired
	private IWechatService wechatService;
	
	@RequiresPermissions("wechat:wechat:view")
	@GetMapping()
	public String wechat()
	{
	    return prefix + "/wechat";
	}
	
	/**
	 * 查询微信相关数据列表
	 */
	@RequiresPermissions("wechat:wechat:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Wechat wechat)
	{
		startPage();
        List<Wechat> list = wechatService.selectWechatList(wechat);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出微信相关数据列表
	 */
	@RequiresPermissions("wechat:wechat:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Wechat wechat)
    {
    	List<Wechat> list = wechatService.selectWechatList(wechat);
        ExcelUtil<Wechat> util = new ExcelUtil<Wechat>(Wechat.class);
        return util.exportExcel(list, "wechat");
    }
	
	/**
	 * 新增微信相关数据
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存微信相关数据
	 */
	@RequiresPermissions("wechat:wechat:add")
	@Log(title = "微信相关数据", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Wechat wechat)
	{		
		return toAjax(wechatService.insertWechat(wechat));
	}

	/**
	 * 修改微信相关数据
	 */
	@GetMapping("/edit/{iD}")
	public String edit(@PathVariable("iD") Integer iD, ModelMap mmap)
	{
		Wechat wechat = wechatService.selectWechatById(iD);
		mmap.put("wechat", wechat);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存微信相关数据
	 */
	@RequiresPermissions("wechat:wechat:edit")
	@Log(title = "微信相关数据", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Wechat wechat)
	{		
		return toAjax(wechatService.updateWechat(wechat));
	}
	
	/**
	 * 删除微信相关数据
	 */
	@RequiresPermissions("wechat:wechat:remove")
	@Log(title = "微信相关数据", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(wechatService.deleteWechatByIds(ids));
	}
	
}
