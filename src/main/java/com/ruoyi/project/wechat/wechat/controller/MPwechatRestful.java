package com.ruoyi.project.wechat.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.project.wechat.wechat.common.AccessToken;
import com.ruoyi.project.wechat.wechat.common.JsapiTicket;
import com.ruoyi.project.wechat.wechat.common.MPopenId;
import com.ruoyi.project.wechat.wechat.common.OpenId;
import com.ruoyi.project.wechat.wechat.domain.*;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import com.ruoyi.project.wechat.wechat.utils.QRCode.QrCodeUtils;
import com.ruoyi.project.wechat.wechat.utils.WechatFormUtil;
import com.ruoyi.project.wechat.wechat.utils.WechatGetUserInfoUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 微信相关数据表
 * <p>
 * Controller
 */

@Controller
@MultipartConfig
@RequestMapping("/mpwechat")
public class MPwechatRestful {

    @Autowired
    private IWechatService wechatService;





    /**
     * 获取access_token
     *
     * @param wechatService
     * @return
     */
    public static String getAccessToken(IWechatService wechatService) {
        try {
            // 查询的实体
            Wechat wechat = new Wechat();

            // 查询
            List<Wechat> wechatList = wechatService.selectWechatList(wechat);

            // 返回数据
            if (wechatList.size() == 0) {
                return null;
            } else {
                /*
                 * create by: lovelyz
                 * description:小程序
                 * create time: 16:22 2020/6/6
                 * params：[wechatService]
                 * @return： java.lang.String
                 */

                wechat = wechatList.get(1);
                if (wechat.getAccessToken() == null || TextUtils.isEmpty(wechat.getAccessToken())) {
                    // 输出
                    System.out.println("WechatController:getAccessToken:=============================数据库中没有accesss_token信息，重新获取");
                    System.out.println("WechatController:getAccessToken:=============================数据库中没有accesss_token信息，重新获取");
                    // 数据库中没有accesss_token信息
                    // 获取accesss_token
                    wechat = wechatList.get(0);
                    String AppID = wechat.getAppId();
                    String AppSecret = wechat.getAppSecret();
                    // 获取
                    String access_token = AccessToken.getAccessToken(AppID, AppSecret).getAccess_token();
                    // 输出
                    System.out.println("WechatController:getAccessToken:=============================" + access_token);
                    System.out.println("WechatController:getAccessToken:=============================" + access_token);

                    if (access_token != null && !TextUtils.isEmpty(access_token)) {
                        // 成功获取accesss_token后，更新数据库
                        wechat.setAccessToken(access_token);
                        wechat.setTokenBuildTime(new Date());
                        // 更新
                        wechatService.updateWechat(wechat);
                        // 返回accesss_token
                        return access_token;
                    }
                } else {
                    // 输出
                    System.out.println("WechatController:getAccessToken:=============================数据库中有accesss_token信息");
                    System.out.println("WechatController:getAccessToken:=============================数据库中有accesss_token信息");

                    if (new Date().getTime() - wechat.getTokenBuildTime().getTime() > 1.5 * 60 * 60 * 1000) {
                        // 输出
                        System.out.println("WechatController:getAccessToken:=============================数据库中有accesss_token信息，但超过1.5个小时，重新获取");
                        System.out.println("WechatController:getAccessToken:=============================数据库中有accesss_token信息，但超过1.5个小时，重新获取");

                        // 数据库中有accesss_token信息，但已经超过有效时间
                        // 获取accesss_token
                        wechat = wechatList.get(0);
                        String AppID = wechat.getAppId();
                        String AppSecret = wechat.getAppSecret();

                        // 获取
                        String access_token = AccessToken.getAccessToken(AppID, AppSecret).getAccess_token();
                        // 输出
                        System.out.println("WechatController:getAccessToken:=============================" + access_token);
                        System.out.println("WechatController:getAccessToken:=============================" + access_token);
                        if (access_token != null && !TextUtils.isEmpty(access_token)) {
                            // 成功获取accesss_token后，更新数据库
                            wechat.setAccessToken(access_token);
                            wechat.setTokenBuildTime(new Date());
                            // 更新
                            wechatService.updateWechat(wechat);
                            // 返回accesss_token
                            return access_token;
                        }
                    } else {
                        // 输出
                        System.out.println("WechatController:getAccessToken:=============================数据库中有accesss_token信息，并且没有超过1.5个小时");
                        System.out.println("WechatController:getAccessToken:=============================数据库中有accesss_token信息，并且没有超过1.5个小时");
                        System.out.println("WechatController:getAccessToken:=============================" + wechat.getAccessToken());
                        System.out.println("WechatController:getAccessToken:=============================" + wechat.getAccessToken());

                        // 数据库中有accesss_token信息，并且没有已经超过有效时间
                        return wechat.getAccessToken();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 默认返回null
        return null;
    }

    /**
     * 微信扫一扫功能
     * <p>
     * 获取appId、jsapi_ticket
     *
     * @param wechatService
     * @return
     */
    public static String[] getAppIdAndJsapiTicket(IWechatService wechatService) {
        String[] returnData = new String[2];

        try {
            // 查询的实体
            Wechat wechat = new Wechat();

            // 查询
            List<Wechat> wechatList = wechatService.selectWechatList(wechat);

            // 返回数据
            if (wechatList.size() == 0) {
                return null;
            } else {
                /*
                 * create by: lovelyz
                 * description:小程序
                 * create time: 16:25 2020/6/6
                 * params：[wechatService]
                 * @return： java.lang.String[]
                 */

                wechat = wechatList.get(1);
                // appId
                returnData[0] = wechat.getAppId();
                // 输出
                System.out.println("WechatController:getAppIdAndJsapiTicket:appId:=============================" + returnData[0]);
                System.out.println("WechatController:getAppIdAndJsapiTicket:appId:=============================" + returnData[0]);

                if (wechat.getJsapiTicket() == null || TextUtils.isEmpty(wechat.getJsapiTicket())) {
                    // 输出
                    System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中没有jsapi_ticket信息，重新获取");
                    System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中没有jsapi_ticket信息，重新获取");

                    // 数据库中没有jsapi_ticket信息
                    // 获取accesss_token
                    String AppID = wechat.getAppId();
                    String AppSecret = wechat.getAppSecret();

                    // 获取accesss_token
                    String access_token = AccessToken.getAccessToken(AppID, AppSecret).getAccess_token();
                    // 输出
                    System.out.println("WechatController:getAppIdAndJsapiTicket:getAccessToken:=============================" + access_token);
                    System.out.println("WechatController:getAppIdAndJsapiTicket:getAccessToken:=============================" + access_token);

                    if (access_token != null && !TextUtils.isEmpty(access_token)) {
                        // 成功获取accesss_token后，获取jsapi_ticket
                        String jsapi_ticket = JsapiTicket.getJsapiTicket(access_token).getTicket();
                        // 输出
                        System.out.println("WechatController:getAppIdAndJsapiTicket:getJsapiticket:=============================" + jsapi_ticket);
                        System.out.println("WechatController:getAppIdAndJsapiTicket:getJsapiticket:=============================" + jsapi_ticket);

                        if (jsapi_ticket != null && !TextUtils.isEmpty(jsapi_ticket)) {
                            // 成功获取jsapi_ticket后，更新数据库
                            wechat.setAccessToken(access_token);
                            wechat.setJsapiTicket(jsapi_ticket);
                            wechat.setTokenBuildTime(new Date());

                            // 更新
                            wechatService.updateWechat(wechat);

                            // 返回数组
                            returnData[1] = jsapi_ticket;
                            return returnData;
                        }
                    }
                } else {
                    // 输出
                    System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中有jsapi_ticket信息");
                    System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中有jsapi_ticket信息");

                    if (new Date().getTime() - wechat.getTokenBuildTime().getTime() > 1.5 * 60 * 60 * 1000) {
                        // 输出
                        System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中有jsapi_ticket信息，但超过1.5个小时，重新获取");
                        System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中有jsapi_ticket信息，但超过1.5个小时，重新获取");
                        // 数据库中有jsapi_ticket信息，但已经超过有效时间
                        // 获取accesss_token
                        String AppID = wechat.getAppId();
                        String AppSecret = wechat.getAppSecret();
                        // 获取accesss_token
                        String access_token = AccessToken.getAccessToken(AppID, AppSecret).getAccess_token();
                        // 输出
                        System.out.println("WechatController:getAppIdAndJsapiTicket:getAccessToken:=============================" + access_token);
                        System.out.println("WechatController:getAppIdAndJsapiTicket:getAccessToken:=============================" + access_token);

                        if (access_token != null && !TextUtils.isEmpty(access_token)) {
                            // 成功获取accesss_token后，获取jsapi_ticket
                            String jsapi_ticket = JsapiTicket.getJsapiTicket(access_token).getTicket();
                            // 输出
                            System.out.println("WechatController:getAppIdAndJsapiTicket:getJsapiticket:=============================" + jsapi_ticket);
                            System.out.println("WechatController:getAppIdAndJsapiTicket:getJsapiticket:=============================" + jsapi_ticket);

                            if (jsapi_ticket != null && !TextUtils.isEmpty(jsapi_ticket)) {
                                // 成功获取jsapi_ticket后，更新数据库
                                wechat.setAccessToken(access_token);
                                wechat.setJsapiTicket(jsapi_ticket);
                                wechat.setTokenBuildTime(new Date());

                                // 更新
                                wechatService.updateWechat(wechat);

                                // 返回数组
                                returnData[1] = jsapi_ticket;
                                return returnData;
                            }
                        }
                    } else {
                        // 数据库中有jsapi_ticket信息，并且没有已经超过有效时间
                        // 输出
                        System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中有jsapi_ticket信息，并且没有超过1.5个小时");
                        System.out.println("WechatController:getAppIdAndJsapiTicket:=============================数据库中有jsapi_ticket信息，并且没有超过1.5个小时");
                        System.out.println("WechatController:getAppIdAndJsapiTicket:=============================" + wechat.getJsapiTicket());
                        System.out.println("WechatController:getAppIdAndJsapiTicket:=============================" + wechat.getJsapiTicket());

                        // 返回数组
                        returnData[1] = wechat.getJsapiTicket();
                        return returnData;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 默认返回null
        return null;
    }

    /**
     * 获取openId
     *
     * @param code
     * @return
     * Created by Lovelyz on 2019/05/22.
     */
    @ResponseBody
    @RequestMapping("/getuserinfo")
    public ResultVO<WechatinfoEntity> getuserinfo(String encryptedData, String iv, String code) {
        ResultVO<WechatinfoEntity> returnData = new ResultVO<WechatinfoEntity>(); // 返回数据
        returnData.setCode(0); // 默认为0
        returnData.setCount(0); // 数据的数量，默认为0
        returnData.setData(null); // 数据List，默认为null
        try {
            if (code == null || code.isEmpty()) {
                returnData.setMsg("code值为空！");
                return returnData;
            }
            // 输出
            System.out.println("WechatRestful:queryOpenId:code:=============================" + code);
            System.out.println("WechatRestful:queryOpenId:code:=============================" + code);
//			openid 实体
            MPopenIdEntity mPopenIdEntity = new MPopenIdEntity();
            String openId; // 返回数据 openid
            String session_key;  //返回的session_key
            WechatinfoEntity wechatinfoEntity = new WechatinfoEntity();
            // 获取微信相关数据
            // 查询的实体
            Wechat wechat = new Wechat();
            // 查询
            List<Wechat> wechatList = wechatService.selectWechatList(wechat);
            if (wechatList == null || wechatList.size() == 0) {
                returnData.setMsg("getuserinfo获取失败！");
            } else {
                // 获取数据
                /*
                 * create by: lovelyz
                 * description:小程序
                 * create time: 16:31 2020/6/6
                 * params：[encryptedData, iv, code]
                 * @return： com.ruoyi.project.wechat.wechat.domain.ResultVO<com.ruoyi.project.wechat.wechat.domain.WechatinfoEntity>
                 */

                wechat = wechatList.get(1);
                String AppID = wechat.getAppId();
                String AppSecret = wechat.getAppSecret();
                // 获取openId
                mPopenIdEntity = MPopenId.getOpenId(AppID, AppSecret, code);
                openId = mPopenIdEntity.getOpenid();
                session_key = mPopenIdEntity.getSession_key();
                // 返回数据
                if (openId == null || TextUtils.isEmpty(openId)) {
                    // 输出
                    System.out.println("WechatRestful:queryOpenId:openId:=============================getuserinfo获取失败");
                    System.out.println("WechatRestful:queryOpenId:openId:=============================getuserinfo获取失败");
                    returnData.setMsg("getuserinfo获取失败！");
                } else {
                    //解密获取用户信息
                    com.alibaba.fastjson.JSONObject userInfoJSON = WechatGetUserInfoUtils.getUserInfo(encryptedData, session_key, iv);
                    System.out.println(userInfoJSON);
                    wechatinfoEntity = JSON.toJavaObject(userInfoJSON, WechatinfoEntity.class);
                    System.out.println(wechatinfoEntity);
                    // 输出
                    System.out.println("WechatRestful:queryOpenId:openId:=============================" + openId);
                    System.out.println("WechatRestful:queryOpenId:openId:=============================" + openId);
                    returnData.setCount(1);
                    returnData.setCode(200);
                    returnData.setMsg(openId);
                    returnData.setDataone(wechatinfoEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            returnData.setMsg("getuserinfo获取失败！");
        }

        return returnData;
    }

    /**
     * 获取推广二维码
     *
     * @param
     * @return
     * @author WJF on 2018/10/12
     */
//    @ResponseBody
//    @RequestMapping("/getQRCode")
//    public ResultVO<WashUser> getExtensionQRCode(HttpServletRequest request, String userId) {
//        ResultVO<WashUser> returnData = new ResultVO<WashUser>(); // 返回数据
//        returnData.setCode(0); // 默认为0
//        returnData.setCount(0); // 数据的数量，默认为0
//        returnData.setData(null); // 数据List，默认为null
//        String path = null;
//        // 更新判断
//        if (userId == null || userId.isEmpty()) {
//            returnData.setMsg("缺少获取条件，推广二维码获取失败！");
//            return returnData;
//        } else {
//            // 获取access_token
//            String access_token = getAccessToken(wechatService);
//            // 获取推广二维码
//            //        生成推广码图片
//            path = QrCodeUtils.CreateCode(request, userId, access_token);
////            WashUser user = new WashUser();
////            user.setUserId(userId); // 用户ID(YHBG+UUID)
////            user.setUserRemark(path); // 用户备注(用户推广二维码图片路径)
////            washUserService.updateByPrimaryKeySelective(user); // 更新
//
//        }
//        // 返回数据
//        returnData.setCount(1);
//        returnData.setMsg(path);
//        return returnData;
//    }


    /**
     * 送服务通知
     *
     * @return
     */
//    @ResponseBody
//    @RequestMapping("/sendTemplateMessage")
//    public ResultVO<WashUser> sendTemplateMessage(HttpServletRequest request, String userId) {
//        ResultVO<WashUser> returnData = new ResultVO<WashUser>(); // 返回数据
//        returnData.setCode(0); // 默认为0
//        returnData.setCount(0); // 数据的数量，默认为0
//        returnData.setData(null); // 数据List，默认为null
//        String openId = "";
//        String templateId = "";
//        String formId = "";
////    for (int j = 0; j < wechatFormList.size(); j++) {
////        Date createTime = wechatFormList.get(j).getCreatedate();
////        Date nowTime = new Date();
////        long cha = nowTime.getTime() - createTime.getTime();
////        if (cha / (1000 * 60 * 60 * 24) > 6) {
////            List<Integer> deleteList = new ArrayList<Integer>();
////            deleteList.add(wechatFormList.get(j).getId());
////            wechatFormList.remove(j);
////            wechatFormService.deleteByIdList(deleteList);
////        }
////    }
////    openId = wechatFormList.get(0).getOpenid();
//        templateId = "UIMqKoW_cRYMlO6Zs9IfHANwvJOs2WVO90poC9ZOlGM";
////    formId = wechatFormList.get(0).getFormid();
//        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String access_token = getAccessToken(wechatService);
//        //String token = "15_UmIZU3I9v8a7awi1Wd-fhTNzaRP2qFujMRMtDgCmoZgxDDxMghRXg7GH57Fr_k28RCbJI5tznJO6tvH927dCRYIoARmaDBx4ZOjCQsZrjI59QfDHZ6YiYCeusDFYTirha23vtI-SnvFptzvTOHNiAIAZSY";
//        String jsonMsg = WechatFormUtil.makeRouteMessage(openId, templateId, formId, "", "#ff6600", "平台确认收款：", null, null, null);
//        boolean result = WechatFormUtil.sendTemplateMessage(access_token, jsonMsg);
//        List<Integer> idList = new ArrayList<Integer>();
////    idList.add(wechatFormList.get(0).getId());
////    wechatFormService.deleteByIdList(idList);
//        System.out.println("服务通知result" + result);
//        System.out.println("服务通知result" + result);
//        return null;
//    }

}
