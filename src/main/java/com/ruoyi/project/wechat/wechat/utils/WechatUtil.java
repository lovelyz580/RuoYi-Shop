package com.ruoyi.project.wechat.wechat.utils;


import com.ruoyi.project.wechat.wechat.common.AccessToken;
import com.ruoyi.project.wechat.wechat.common.JsapiTicket;
import com.ruoyi.project.wechat.wechat.domain.Wechat;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Lovelyz
 * on 2019-10-10 11:44
 */
@Component
public class WechatUtil {


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
          List<Wechat> wechats = wechatService.selectWechatList(wechat);

          wechat = wechats.get(0);
            // 返回数据
            if (wechat == null) {
                return null;
            } else {
                if (wechat.getAccessToken() == null || TextUtils.isEmpty(wechat.getAccessToken())) {
                    // 输出
                    System.out.println("WechatController:getAccessToken:=============================数据库中没有accesss_token信息，重新获取");
                    System.out.println("WechatController:getAccessToken:=============================数据库中没有accesss_token信息，重新获取");
                    // 数据库中没有accesss_token信息
                    // 获取accesss_token
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

         List<Wechat>   wechats = wechatService.selectWechatList(wechat);
wechat = wechats.get(0);
            // 返回数据
            if (wechat == null) {
                return null;
            } else {
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
}
