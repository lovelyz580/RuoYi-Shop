package com.ruoyi.project.wechat.wechat.controller;

import com.alibaba.fastjson.JSON;


import com.ruoyi.project.common.MyLog;
import com.ruoyi.project.wechat.wechat.common.OpenId;
import com.ruoyi.project.wechat.wechat.domain.ResultVO;
import com.ruoyi.project.wechat.wechat.domain.Wechat;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import com.ruoyi.project.wechat.wechat.utils.SHA1Utils;
import com.ruoyi.project.wechat.wechat.utils.WechatUtil;
import io.swagger.annotations.Api;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 微信相关数据表
 * <p>
 * Controller
 */

@Controller
@RequestMapping("/oawechat")
@Api(tags = "微信")
public class OAwechatController {

    @Autowired
    private IWechatService wechatService;



    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取openId
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/selectopenid")
    @ResponseBody
    @MyLog(value = "获取openID")  //这里添加了AOP的自定义注解
    public ResultVO selectOpenid(String code) {
        ResultVO returnMessage = new ResultVO(); // 返回数据
        try {
            String openId; // 返回数据
            System.out.println(code);
            // 输出
            logger.error("WechatController:queryOpenId:code:=============================" + code);
            System.out.println("WechatController:queryOpenId:code:=============================" + code);

            // 查询AppID、AppSecret
            // 查询的实体
            Wechat wechat = new Wechat();

            // 查询
        List<Wechat>    wechats = wechatService.selectWechatList(wechat);
            wechat = wechats.get(0);
            // 返回数据
            if (wechat == null) {
                return null;
            } else {
                // 获取AppID、AppSecret
                String AppID = wechat.getAppId();
                String AppSecret = wechat.getAppSecret();

                // 获取openId
                openId = OpenId.getOpenId(AppID, AppSecret, code).getOpenid();
            }

            // 返回数据
            if (openId == null || TextUtils.isEmpty(openId)) {
                // 输出
                logger.error("WechatController:queryOpenId:openId:=============================openId获取失败");
                System.out.println("WechatController:queryOpenId:openId:=============================openId获取失败");

                returnMessage.setCode(300);
                returnMessage.setMsg("openId获取失败！");
            } else {
                // 输出
                logger.error("WechatController:queryOpenId:openId:=============================" + openId);
                System.out.println("WechatController:queryOpenId:openId:=============================" + openId);
                returnMessage.setCode(200);
                returnMessage.setMsg(openId);
            }
        } catch (Exception e) {
            e.printStackTrace();

            returnMessage.setCode(300);
            returnMessage.setMsg("openId获取失败！");
        }

        return returnMessage;
    }


    /**
     * wechat
     * 获取用户个人信息
     *
     * @param openid
     * @return
     */
    @RequestMapping(value = "/getuserinfo")
    @ResponseBody
    @MyLog(value = "获取用户个人信息")  //这里添加了AOP的自定义注解
    public JSONObject getuserinfo(String openid) {
        /**
         * //        测试数据
         *   openid = "oXXSa1LTXn-EjfHtKLBo6m2RqTn4";
         */
        String access_token = WechatUtil.getAccessToken(wechatService);
        String infoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        System.out.println("infoUrl:" + infoUrl);
        JSONObject userInfo = null;
        try {
            userInfo = doGetJson(infoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON-----" + userInfo.toString());
        return userInfo;
    }

    /**
     * 获取access_token
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectAccessToken", method = RequestMethod.POST)
    @ResponseBody
    @MyLog(value = "获取access_token")  //这里添加了AOP的自定义注解
    public ResultVO selectAccessToken(HttpServletRequest request) {
        ResultVO returnMessage = new ResultVO(); // 返回数据
        // 获取access_token
        String access_token = WechatUtil.getAccessToken(wechatService);
        // 返回数据
        if (access_token == null || TextUtils.isEmpty(access_token)) {
            returnMessage.setCode(3000);
            returnMessage.setMsg("access_token获取失败！");
        } else {
            returnMessage.setCode(200);
            returnMessage.setMsg(access_token);
        }

        return returnMessage;
    }

    /**
     * 微信扫一扫功能
     * <p>
     * 获取addId、signature
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectSignature", method = RequestMethod.POST)
    @ResponseBody
    @MyLog(value = "微信扫一扫功能")  //这里添加了AOP的自定义注解
    public ResultVO selectSignature(HttpServletRequest request) {
        /**
         * 微信扫一扫功能
         *
         * 微信JS接口签名校验工具:https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=jsapisign
         *
         * 获取signature方法:
         *
         * jsapi_ticket生成方法:
         *	 1、获取access_token:https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=XXX&secret=XXXX
         * 	 2、获取jsapi_ticket:https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=XXX&type=jsapi
         *
         * url:需要调用JS接口的URL:例如:http://yyme.yibaichina.com.cn/Expand/gl-my.html
         */

        ResultVO returnMessage = new ResultVO(); // 返回数据
        try {
            // 获取appId、jsapi_ticket
            String[] retrunData = WechatUtil.getAppIdAndJsapiTicket(wechatService);
            String appId = retrunData[0];
            String jsapi_ticket = retrunData[1];

            if (jsapi_ticket == null || TextUtils.isEmpty(jsapi_ticket)) {
                // 返回数据
                returnMessage.setCode(300);
                returnMessage.setMsg("jsapi_ticket获取失败！");
            } else {
                // 获取signature方法
                // signature = sha1(string1);

                // noncestr 生成的随机字符串
                String nonceStr = request.getParameter("nonceStr");

                // timestamp 时间戳
                String timeStamp = request.getParameter("timeStamp");

                // url 需要调用JS接口的URL
                String URL = request.getParameter("URL");

                String string = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + URL;

                String signature = SHA1Utils.encode(string);

                // 输出
                logger.error("WechatController:querySignature:=============================" + signature);
                System.out.println("WechatController:querySignature:=============================" + signature);


                // 返回数据
                String returnData = appId + "," + signature;

                returnMessage.setCode(200);
                returnMessage.setMsg(returnData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMessage;
    }

    /**
     * 发送模板消息
     * 示例
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendTemplate", method = RequestMethod.POST)
    @ResponseBody
    @MyLog(value = "发送模板消息")  //这里添加了AOP的自定义注解
    public ResultVO sendTemplate(HttpServletRequest request) {
          String noticeId = request.getParameter("noticeId");
//        DisNotice notice = new DisNotice();
//        notice.setNoticeId(Integer.valueOf(noticeId));
//        ResultVO returnMessage =  disNoticeService.sendByDisNotice(notice);

        return null;

    }




//    /**
//     * 发送模板消息
//     * 示例
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/sendTemplate", method = RequestMethod.POST)
//    @ResponseBody
//    @MyLog(value = "发送模板消息")  //这里添加了AOP的自定义注解
//    public ResultVO sendTemplate(HttpServletRequest request) {
//        ResultVO returnMessage = new ResultVO(); // 返回数据
//        String access_token = null;
//        String template_id = null;
//        try {
//            // 获取access_token
//            access_token = WechatUtil.getAccessToken(wechatService);
//            // 输出
//            logger.error("WechatController:sendTemplate:access_token:=============================" + access_token);
//            System.out.println("WechatController:sendTemplate:access_token:=============================" + access_token);
//
//            // 判断数据
//            if (access_token == null || TextUtils.isEmpty(access_token)) {
//                returnMessage.setCode(300);
//                returnMessage.setMsg("access_token获取失败！");
//                return returnMessage;
//            }
//            // 根据模板库的模板编号获得我的模板消息的模板id
//            String template_id_short = request.getParameter("template_id_short"); // 模板库的模板编号
////			String template_id_short = WechatConfig.WECHAT_TEMPLATE_ID_APPT_SUCCESS; // 模板库的模板编号
//            template_id = Template.getTemplate(access_token, template_id_short).getTemplate_id();
////            template_id = "k4lyopLs1s3Hjq_xl5E7-KX6j5nN-yQbU00RnvTFBpo";
//            // 输出
//            logger.error("WechatController:sendTemplate:template_id:=============================" + template_id);
//            System.out.println("WechatController:sendTemplate:template_id:=============================" + template_id);
//
//            // 判断数据
//            if (template_id == null || TextUtils.isEmpty(template_id)) {
//                returnMessage.setCode(300);
//                returnMessage.setMsg("template_id获取失败！");
//                return returnMessage;
//            }
//
//            // openId
//            String openId = request.getParameter("openId");
////            String openId ="oXXSa1LTXn-EjfHtKLBo6m2RqTn4";
//                    // 输出
//            logger.error("WechatController:sendTemplate:openId:=============================" + openId);
//            System.out.println("WechatController:sendTemplate:openId:=============================" + openId);
//            // 模板消息
//            // 模板消息标题
//            String title = request.getParameter("title");
////			String title = "测试模板消息标题";
//            List<String> dataList = new ArrayList<String>();
//
//            // 模板消息数据
//            String context = request.getParameter("context");
////			String context = "1|2|3|4";
//            String contextArray[] = context.split("|");
//            for (int i = 0; i < contextArray.length; i++) {
//                if (i == 0 || i % 2 == 0) {
//                    continue;
//                }
//                dataList.add(contextArray[i]);
//            }
//
//
//            // 模板消息备注
//            String remark = request.getParameter("noticeContents");
//			//String remark = "测试模板消息备注";
//            Integer noticeTogroup = Integer.parseInt(request.getParameter("noticeTogroup"));
//
//            List<DisUser> userList = new ArrayList<DisUser>();
//            DisUser selectUser;
//            if (noticeTogroup == 3){
//                //通知全部的人
//                selectUser = new DisUser();
//                selectUser.setUserIsdel(1);//未删除
//                selectUser.setPagenumber(-1);//不分页
//                userList =  disUserMapper.selectByDisUsers(selectUser);
//
//            }else {
//                //通知选定的人
//                selectUser = new DisUser();
//                selectUser.setUserIsdel(1);//未删除
//                selectUser.setUserGrade(noticeTogroup);
//                selectUser.setPagenumber(-1);//不分页
//                userList =  disUserMapper.selectByDisUsers(selectUser);
//            }
//            // 模板消息返回的结果
//            //TemplateSendEntity templateSend = Template.sendTemplate(access_token, openId, template_id, title, dataList, remark);
//            TemplateSendEntity templateSend = new TemplateSendEntity();
//            if (userList.size() > 0){
//                for (DisUser user : userList) {
//                    templateSend = Template.sendTemplate(access_token, user.getUserOpenid(), template_id, title, dataList, remark);
//                }
//
//            }
//            String errMsg = templateSend.getErrmsg();
//            long msgId = templateSend.getMsgid();
//            // 输出
//            logger.error("WechatController:sendTemplate:errMsg:=============================" + errMsg);
//            System.out.println("WechatController:sendTemplate:errMsg:=============================" + errMsg);
//
//            // 根据我的模板消息的模板id删除模板
//           /* TemplateDeleteEntity templateDelete = Template.deleteTemplate(access_token, template_id);
//            // 输出
//            logger.error("WechatController:deleteTemplate:errMsg:=============================" + templateDelete.getErrmsg());
//            System.out.println("WechatController:deleteTemplate:errMsg:=============================" + templateDelete.getErrmsg());*/
//
//            // 返回数据
//            if ("ok".equals(errMsg)) {
//                returnMessage.setCode(200);
//                returnMessage.setMsg(String.valueOf(msgId));
//            } else {
//                returnMessage.setCode(300);
//                returnMessage.setMsg("发送模板消息失败！");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            returnMessage.setCode(300);
//            returnMessage.setMsg("发送模板消息失败！");
//        } /*finally {
//            if (access_token != null && template_id != null) {
//                // 根据我的模板消息的模板id删除模板
//                TemplateDeleteEntity templateDelete = Template.deleteTemplate(access_token, template_id);
//                // 输出
//                logger.error("WechatController:finally:deleteTemplate:errMsg:=============================" + templateDelete.getErrmsg());
//                System.out.println("WechatController:finally:deleteTemplate:errMsg:=============================" + templateDelete.getErrmsg());
//            }
//        }*/
//
//        return returnMessage;
//    }









    public static JSONObject doGetJson(String url) throws IOException {
        com.alibaba.fastjson.JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // 把返回的结果转换为JSON对象
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSON.parseObject(result);
        }
        return JSONObject.fromObject(jsonObject);
    }


    /**
     * 获取config初始化参数
     * 自定义分享
     *
     * @return
     */
    @RequestMapping("/initWXJSInterface")
    @ResponseBody
    @MyLog(value = "获取config初始化参数")  //这里添加了AOP的自定义注解
    public Map<String, Object> getSignature(HttpServletRequest request) {
        ResultVO returnMessage = new ResultVO(); // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();

        String weburl = request.getParameter("link");

        Long timestamp = System.currentTimeMillis() / 1000;
        int noncestr = new Random().nextInt();
        // 获取appId、jsapi_ticket
        String[] retrunData = WechatUtil.getAppIdAndJsapiTicket(wechatService);
        String appId = retrunData[0];
        String jsapi_ticket = retrunData[1];
        if (jsapi_ticket == null || TextUtils.isEmpty(jsapi_ticket)) {
            // 返回数据
            returnMessage.setCode(300);
            returnMessage.setMsg("jsapi_ticket获取失败！");
        } else {
            //生成signature
            List<String> nameList = new ArrayList<String>();
            nameList.add("noncestr");
            nameList.add("timestamp");
            nameList.add("url");
            nameList.add("jsapi_ticket");
            Map<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put("noncestr", noncestr);
            valueMap.put("timestamp", timestamp);
            valueMap.put("url", weburl);
            valueMap.put("jsapi_ticket", jsapi_ticket);
            Collections.sort(nameList);
            String origin = "";
            for (int i = 0; i < nameList.size(); i++) {
                origin += nameList.get(i) + "=" + valueMap.get(nameList.get(i)).toString() + "&";
            }
            origin = origin.substring(0, origin.length() - 1);
            String signature = SHA1(origin);
            map = new HashMap<String, Object>();
            map.put("jsapi_ticket", jsapi_ticket);
            map.put("appId", appId);
            map.put("signature", signature.toLowerCase());
            map.put("timestamp", timestamp.toString());
            map.put("noncestr", String.valueOf(noncestr));
        }
        return map;
    }

    /**
     * 根据Wechat实体类查询
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectWechat", method = RequestMethod.POST)
    @ResponseBody
    @MyLog(value = "根据Wechat实体类查询")  //这里添加了AOP的自定义注解
    public ResultVO selectWechat(HttpServletRequest request) {
        ResultVO returnData = new ResultVO(); // 返回数据
        try {
            // 获取到传递过来的数据
            // 查询的实体
            Wechat wechat = new Wechat();
       List<Wechat>     wechats = wechatService.selectWechatList(wechat);
            wechat = wechats.get(0);
            // 返回数据
            if (wechat == null) {
                returnData.setCode(300);
                returnData.setMsg("未找到相关数据！");
                returnData.setDataone(null);
            } else {
                returnData.setCode(200);
                returnData.setMsg("找到相关数据！");
                returnData.setDataone(wechat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnData;
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @throws Exception
     * @Method 微信图片下载到服务器
     * @author
     * @createTime
     */
    @ResponseBody
    @RequestMapping("uploadimg")
    public String uploadimages(String mediaId, HttpServletRequest request) throws Exception {
//    获取微信图片
        InputStream input = null;
        FileOutputStream output = null;
        try {
            // 从微信获取的流,这个utils就是根据返回mediaId后去流的
            input = getInputStream(mediaId);
            String src = "upload";
            // 获取真实路径
            String path = request.getServletContext().getRealPath("/" + src);
            File folder = new File(path);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File targetFile = new File(folder, new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg");
            output = new FileOutputStream(targetFile);
            IOUtils.copy(input, output);
            //上边就是把图片保存到服务器里
            //下边是数据库的一些操作
            // 如果数据库有就删除

            String imgname = targetFile.getName();

            return imgname;
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }




    /**
     * 根据文件id下载文件
     *
     * @param mediaId 媒体id
     * @throws IOException
     * @throws Exception
     */
    public  InputStream getInputStream(String mediaId) throws IOException {
        InputStream is = null;

        String access_token = WechatUtil.getAccessToken(wechatService);
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + access_token + "&media_id=" + mediaId;
        //根据AccessToken获取media
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            is = http.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is;
    }

//    /**
//     * 获取下载图片信息（jpg）
//     *
//     * @param mediaId 文件的id
//     * @throws Exception
//     */
//
//    public void saveImageToDisk(String mediaId) throws Exception {
//        InputStream inputStream = getInputStream(mediaId);
//        byte[] data = new byte[1024];
//        int len = 0;
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream("test1.jpg");
//            while ((len = inputStream.read(data)) != -1) {
//                fileOutputStream.write(data, 0, len);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }



}
