package com.ruoyi.project.wechat.wechat.controller;

import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import com.ruoyi.project.wechat.wechat.domain.Wechat;
import com.ruoyi.project.wechat.wechat.pay.PayToolUtils;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import com.ruoyi.project.wechat.wechat.utils.HttpPayUtils;
import com.ruoyi.project.wechat.wechat.utils.XMLUtil4jdom;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 微信支付
 * <p>
 * Controller
 * <p>
 * Created by Lovelyz on 2019/05/22.
 */
@Controller
@RequestMapping("/mpwechatpay")
public class MPwechatPayRestful {

    @Autowired
    private IWechatService wechatService;
  




    /*
     * 微信支付
     * <p>
     * 公众号支付时，返回预支付交易会话标识
     * 扫码支付时，返回二维码链接
     * create by: lovelyz
     * description:
     * create time: 16:17 2020/6/6
     * @param userId    根据userId获取对应的openId，当trade_type为JSAPI时(即公众号支付)，openId参数必传
     * @param tradeType 交易类型 JSAPI:公众号支付(小程序)  NATIVE:扫码支付  APP:APP支付
     * @param money     金额 double类型，两位小数，例如：1.00
     * @return
     */
//    @ResponseBody
//    @RequestMapping("/getPrepayId")
//    public LayuiDataTemplet<WashUser> getPrepayId(String userId, String orderId, String importantId,String goodName, String couponId, String tradeType, Double money) {
//        LayuiDataTemplet<WashUser> returnData = new LayuiDataTemplet<WashUser>(); // 返回数据
//        returnData.setCode(0); // 默认为0
//        returnData.setCount(0); // 数据的数量，默认为0
//        returnData.setData(null); // 数据List，默认为null
//        try {
//            String message = null;
//            // 根据userId获取对应的openId
//            String openId = null;
//            // 查询的实体
//            WashUser unionData = new WashUser();
//            unionData.setUserId(userId); // 用户ID(YHBG+UUID)
//            unionData.setPagenumber(-1); // 当前页数(如果不进行分页，该条数据默认为-1)
//
//            // 查询
//            List<WashUser> userList = washUserService.selectByCondition(unionData);
//            if (userList == null || userList.size() == 0) {
//                returnData.setMsg("openId获取失败，prepay_id获取失败！");
//            } else {
//                openId = userList.get(0).getUserOpenid();
//                if (openId == null || openId.isEmpty()) {
//                    returnData.setMsg("该用户openId为空，prepay_id获取失败！");
//                } else {
//                    // 获取微信相关数据
//                    // 查询的实体
//                    Wechat wechat = new Wechat();
//
//                    // 查询
//                    List<Wechat> wechatList = wechatService.selectWechatList(wechat);
//
//                    if (wechatList != null && wechatList.size() != 0) {
//                        /*
//                         * create by: lovelyz
//                         * description: //小程序
//                         * create time: 16:13 2020/6/6
//                         * params：[userId, orderId, importantId, goodName, couponId, tradeType, money]
//                         * @return： LayuiDataTemplet<WashUser>
//                         */
//
//                        wechat = wechatList.get(1);
//                        // 随机字符串
//                        String currTime = PayToolUtils.getCurrTime();
//                        String timeStr = currTime.substring(8, currTime.length());
//                        String randomStr = PayToolUtils.buildRandom(4) + "";
//                        String nonce_str = timeStr + randomStr;
//                        // 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
//                        // 这里放入的是业务中的用户Id和金额
////                        订单ID  用户ID  支付金额 优惠券ID
//                        String attach = userId + "," + orderId + "," + money + "," + couponId + ',' + importantId;
//
//                        // 商户订单号
//                        String out_trade_no = "" + System.currentTimeMillis();
//
//                        // 商品价格
//                        // 保留2位小数，四舍五入，并将double转化为Stirng
//                        String goodMoneyStr = String.valueOf(Math.round(money * 100));
//
//                        // 为微信支付添加参数
//                        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
//                        packageParams.put("appid", wechat.getAppId()); // 公众账号ID
//                        packageParams.put("mch_id", wechat.getMCHID()); // 商户号
//                        packageParams.put("nonce_str", nonce_str); // 随机字符串
//                        packageParams.put("body", goodName); // 商品描述  // 商品名称
//                        packageParams.put("attach", attach); // 附加数据，非必填字段
//                        packageParams.put("out_trade_no", out_trade_no); // 商户订单号
//                        packageParams.put("total_fee", goodMoneyStr); // 价格的单位为分
//                        packageParams.put("spbill_create_ip", wechat.getPayIp()); // 终端IP
//                        packageParams.put("notify_url", wechat.getPayNotifyUrl()); // 微信支付回调接口
//                        packageParams.put("trade_type", tradeType); // 交易类型 JSAPI:公众号支付  NATIVE:扫码支付  APP:APP支付
//                        packageParams.put("sign_type", "MD5"); // 签名类型，默认为MD5
//                        if ("JSAPI".equals(tradeType)) {
//                            packageParams.put("openid", openId); // 用户标识，当交易类型为JSAPI时(即公众号支付)，此参数必传
//                        }
//                        // 签名
//                        // 如果参数的值为空不参与签名
//                        String sign = PayToolUtils.createSign("UTF-8", packageParams, wechat.getAPI());
//                        packageParams.put("sign", sign); // 签名
//
//                        // 将SortedMap转换为xml类型的String
//                        String requestXML = PayToolUtils.getRequestXml(packageParams);
//                        // 输出
//                        System.out.println("WechatPayRestful:支付-requestXML:=============================" + requestXML);
//                        System.out.println("WechatPayRestful:支付-requestXML:=============================" + requestXML);
//
//                        // 返回信息
//                        String responseXml = HttpPayUtils.postData(OAwechatConfig.WECHAT_PAY_URL, requestXML);
//                        // 输出
//                        System.out.println("WechatPayRestful:支付-responseXml:=============================" + responseXml);
//                        System.out.println("WechatPayRestful:支付-responseXml:=============================" + responseXml);
//
//                        // 获取数据
//                        if ("JSAPI".equals(tradeType)) {
//                            // 公众号支付时，获取返回的预支付交易会话标识
//                            Map map = XMLUtil4jdom.doXMLParse(responseXml);
//                            message = (String) map.get("prepay_id");
//                            // 输出
//                            System.out.println("WechatPayRestful:支付-responseXml:prepay_id:=============================" + returnData);
//                            System.out.println("WechatPayRestful:支付-responseXml:prepay_id:=============================" + returnData);
//                        } else if ("NATIVE".equals(tradeType)) {
//                            // 扫码支付时，获取返回的二维码链接
//                            Map map = XMLUtil4jdom.doXMLParse(responseXml);
//                            message = (String) map.get("code_url");
//                            // 输出
//                            System.out.println("WechatPayRestful:支付-responseXml:code_url:=============================" + returnData);
//                            System.out.println("WechatPayRestful:支付-responseXml:code_url:=============================" + returnData);
//                        }
//                    }
//
//                    // 返回数据
//                    if (message == null || message.isEmpty()) {
//                        returnData.setMsg("prepay_id获取失败！");
//                    } else {
//                        returnData.setCount(1);
//                        returnData.setMsg(message);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return returnData;
//    }

    /**
     * 微信支付
     *
     * @param prepayId 预支付交易会话标识
     * @return Created by Lovelyz on 2019/05/22.
     */
//    @ResponseBody
//    @RequestMapping("/pay")
//    public LayuiDataTemplet<WashUser> pay(String prepayId) {
//        LayuiDataTemplet<WashUser> returnData = new LayuiDataTemplet<WashUser>(); // 返回数据
//        returnData.setCode(0); // 默认为0
//        returnData.setCount(0); // 数据的数量，默认为0
//        returnData.setData(null); // 数据List，默认为null
//
//        // 在微信浏览器里面打开H5网页中执行JS调起支付
//        // 公众号支付时，根据返回的预支付交易会话标识，获取签名
//        String message = null;
//
//        try {
//            // 获取微信相关数据
//            // 查询的实体
//            Wechat wechat = new Wechat();
//
//            // 查询
//            List<Wechat> wechatList = wechatService.selectByWechat(wechat);
//
//            if (wechatList == null || wechatList.size() == 0) {
//                returnData.setMsg("openId获取失败，微信支付失败！");
//            } else {
//                /*
//                 * create by: lovelyz
//                 * description:小程序
//                 * create time: 16:18 2020/6/6
//                 * params：[prepayId]
//                 * @return： LayuiDataTemplet<WashUser>
//                 */
//
//                wechat = wechatList.get(1);
//                // 随机字符串
//                String currTime = PayToolUtils.getCurrTime();
//                String timeStr = currTime.substring(8, currTime.length());
//                String randomStr = PayToolUtils.buildRandom(4) + "";
//                String nonce_str = timeStr + randomStr;
//
//                // 为微信支付添加参数
//                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
//                packageParams.put("appId", wechat.getAppId()); // 公众账号ID
//                packageParams.put("timeStamp", "1414587457"); // 时间戳
//                packageParams.put("nonceStr", nonce_str); // 随机字符串
//                packageParams.put("package", "prepay_id=" + prepayId); // 公众号支付时，获取返回的预支付交易会话标识
//                packageParams.put("signType", "MD5"); // 签名类型，默认为MD5
//                // 签名
//                // 如果参数的值为空不参与签名
//                String sign = PayToolUtils.createSign("UTF-8", packageParams, wechat.getAPI());
//                packageParams.put("paySign", sign); // 签名
//
//                // 返回数据
//                message = "appId=" + wechat.getAppId() + "&nonceStr=" + nonce_str + "&paySign=" + sign;
//            }
//
//            // 返回数据
//            if (message == null || message.isEmpty()) {
//                returnData.setMsg("微信支付失败！");
//            } else {
//                returnData.setCount(1);
//                returnData.setMsg(message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return returnData;
//    }

    /**
     * 微信支付回调接口
     * <p>
     * 扫码支付回调接口
     * <p>
     * 业务逻辑：
     * 1、更新订单状态
     * 2、会员充值 更新会员状态
     * 3、使用优惠券 更新优惠券使用次数  添加优惠券使用记录
     *
     * @param request
     * @param response
     * @throws JDOMException
     * @throws Exception
     * @author WJF on 2018/10/13
     */
    @ResponseBody
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request, HttpServletResponse response) throws JDOMException, Exception {
        // 输出
        System.out.println("WechatPayRestful:支付回调=============================进入回调接口");
        System.out.println("WechatPayRestful:支付回调=============================进入回调接口");

        // 读取参数
        StringBuffer sb = new StringBuffer();
        String s;
        InputStream inputStream = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        // 解析xml成map
        Map<String, String> map = new HashMap<String, String>();
        map = XMLUtil4jdom.doXMLParse(sb.toString());
        // 过滤空数据
        // 设置TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = map.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 输出
        System.out.println("WechatPayRestful:支付回调-packageParams:=============================" + packageParams);
        System.out.println("WechatPayRestful:支付回调-packageParams:=============================" + packageParams);

        // 获取附加数据中的业务中的商户Id和订单Id
        String attach = (String) packageParams.get("attach");

        System.out.println("WechatPayRestful:支付回调-业务中的attach:=============================" + attach);
        String[] strs = attach.split(",");
        String userId = strs[0].toString();   //用户ID
        String orderId = strs[1].toString(); //订单ID
        String moneyStr = strs[2].toString();  //付款金额
        String couponId = strs[3].toString(); //优惠券ID
        String importantId = strs[4].toString();  //会员充值ID
        double money = Double.valueOf(moneyStr);
        // 输出
        System.out.println("WechatPayRestful:支付回调-业务中的attach:=============================" + attach);
        System.out.println("WechatPayRestful:支付回调-业务中的用户Id:=============================" + userId);
        System.out.println("WechatPayRestful:支付回调-业务中的金额:=============================" + money);
        System.out.println("WechatPayRestful:支付回调-业务中的订单id:=============================" + orderId);
        System.out.println("WechatPayRestful:支付回调-业务中的用户Id:=============================" + userId);
        System.out.println("WechatPayRestful:支付回调-业务中的金额:=============================" + money);
        System.out.println("WechatPayRestful:支付回调-业务中的订单id:=============================" + orderId);
        System.out.println("WechatPayRestful:支付回调-业务中的优惠券id:=============================" + couponId);
        System.out.println("WechatPayRestful:支付回调-业务中的会员充值id:=============================" + importantId);
        // 获取微信相关数据
        // 查询的实体
        Wechat wechat = new Wechat();

        // 查询
        List<Wechat> wechatList = wechatService.selectWechatList(wechat);

        if (wechatList != null && wechatList.size() != 0) {
            /*
             * create by: lovelyz
             * description:小程序
             * create time: 16:19 2020/6/6
             * params：[request, response]
             * @return： java.lang.String
             */

            wechat = wechatList.get(1);
            // 根据公众账号ID，判断签名是否正确
            if (PayToolUtils.isTenpaySign("UTF-8", packageParams, wechat.getAPI())) {
                // 输出
                System.out.println("WechatPayRestful:支付回调=============================签名验证正确，处理业务开始");
                System.out.println("WechatPayRestful:支付回调=============================签名验证正确，处理业务开始");

                // 处理业务开始
                String responseXml = ""; // 通知微信接收回调成功
                if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                    // 输出
                    System.out.println("WechatPayRestful:支付回调=============================支付成功");
                    System.out.println("WechatPayRestful:支付回调=============================支付成功");

                    // 支付成功
                    // 通知微信，异步确认成功
                    // 必写，不然会一直通知后台，八次之后就认为交易失败
                    responseXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                    // 业务逻辑

                } else {
                    // 输出
                    System.out.println("WechatPayRestful:支付回调=============================支付失败");
                    System.out.println("WechatPayRestful:支付回调=============================支付失败");

                    responseXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }

                // 通知微信接收回调成功
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(responseXml.getBytes());
                out.flush();
                out.close();

                // 处理业务完毕
                // 输出
                System.out.println("WechatPayRestful:支付回调=============================签名验证正确，处理业务结束");
                System.out.println("WechatPayRestful:支付回调=============================签名验证正确，处理业务结束");
            } else {
                // 输出
                System.out.println("WechatPayRestful:支付回调=============================签名验证失败");
                System.out.println("WechatPayRestful:支付回调=============================签名验证失败");
            }
        }

        // TODO 跳转页面
//		return "yh-wechat-pay-fail.html";

        return null;
    }
}
