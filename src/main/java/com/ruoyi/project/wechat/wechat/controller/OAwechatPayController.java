package com.ruoyi.project.wechat.wechat.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import com.ruoyi.project.wechat.wechat.domain.ResultVO;
import com.ruoyi.project.wechat.wechat.domain.Wechat;
import com.ruoyi.project.wechat.wechat.pay.PayToolUtils;
import com.ruoyi.project.wechat.wechat.service.IWechatService;
import com.ruoyi.project.wechat.wechat.utils.HttpPayUtils;
import com.ruoyi.project.wechat.wechat.utils.QRCode.BuildQRCode;
import com.ruoyi.project.wechat.wechat.utils.XMLUtil4jdom;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.http.util.TextUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 微信支付
 * <p>
 * Controller
 */

@Controller
@RequestMapping("/oawechatpay")
public class OAwechatPayController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IWechatService wechatService;


    // 时间格式化
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    /**
     * 微信支付
     * <p>
     * 公众号支付时，返回预支付交易会话标识
     * 扫码支付时，返回二维码链接
     *
     * @return
     */
    @RequestMapping(value = "/getPrepayId.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultVO getPrepayId(HttpServletRequest request) {
        ResultVO returnMessage = new ResultVO(); // 返回数据
        try {
            // 获取到传递过来的数据
            // 用户openId
            // 当trade_type为JSAPI时(即公众号支付)，openId参数必传
            String openId = request.getParameter("openid");

            // 交易类型
            // JSAPI:公众号支付(小程序)
            // MWEB:H5支付
            // NATIVE:扫码支付
            // APP:APP支付
            String tradeType = request.getParameter("tradetype");

            // 商品名称
            String goodName = request.getParameter("goodname");
//            goodName = toUtf8String(goodName);

            // 附加数据，非必填字段，在查询API和支付通知中原样返回，可作为自定义参数使用
            String attach = request.getParameter("attach");

            // 场景信息
            // 当trade_type为MWEB时(即H5支付)，sceneInfo参数必传
            String sceneInfo = request.getParameter("scene_info");

            // 金额
            // 两位小数，例如：1.00
            Double money = null;
            String moneyString = request.getParameter("money");
            if (moneyString != null && !moneyString.isEmpty()) {
                money = Double.parseDouble(moneyString);
            }

            // 判断参数
            if (money == null || tradeType == null || tradeType.isEmpty() || goodName == null || goodName.isEmpty()) {
                returnMessage.setCount(0);
                returnMessage.setMsg("支付参数为空，支付失败！");
                return returnMessage;
            } else {
                if ("JSAPI".equals(tradeType)) {
                    if (openId == null || openId.isEmpty()) {
                        returnMessage.setCount(0);
                        returnMessage.setMsg("支付参数为空，支付失败！");
                        return returnMessage;
                    }
                }
            }

            // 返回数据
            String message = null;

            // 获取微信相关数据
            // 查询的实体
            Wechat wechat = new Wechat();

            // 查询
            List<Wechat> wechatList = wechatService.selectWechatList(wechat);

            if (wechatList != null ) {
                wechat = wechatList.get(0);
                // 随机字符串
                String currTime = PayToolUtils.getCurrTime();
                String timeStr = currTime.substring(8, currTime.length());
                String randomStr = PayToolUtils.buildRandom(4) + "";
                String nonce_str = timeStr + randomStr;

                // 商户订单号
                String out_trade_no = "" + System.currentTimeMillis();

                // 商品价格
                // 保留2位小数，四舍五入，并将double转化为Stirng
                String goodMoneyStr = String.valueOf(Math.round(money * 100));

                // 为微信支付添加参数
                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
                packageParams.put("appid", wechat.getAppId()); // 公众账号ID
                packageParams.put("mch_id", wechat.getMCHID()); // 商户号
                packageParams.put("nonce_str", nonce_str); // 随机字符串
                packageParams.put("body", goodName); // 商品描述
                packageParams.put("attach", attach); // 附加数据，非必填字段，在查询API和支付通知中原样返回，可作为自定义参数使用
                packageParams.put("out_trade_no", out_trade_no); // 商户订单号
                packageParams.put("total_fee", goodMoneyStr); // 价格的单位为分
                packageParams.put("spbill_create_ip", wechat.getPayIp()); // 终端IP
                packageParams.put("notify_url", wechat.getPayNotifyUrl()); // 微信支付回调接口
                packageParams.put("trade_type", tradeType); // 交易类型 JSAPI:公众号支付  MWEB:H5支付  NATIVE:扫码支付  APP:APP支付
//                packageParams.put("sign_type", "MD5"); // 签名类型，默认为MD5
                if ("JSAPI".equals(tradeType)) {
                    packageParams.put("openid", openId); // 用户标识，当交易类型为JSAPI时(即公众号支付)，此参数必传
                }
                if ("MWEB".equals(tradeType)) {
                    packageParams.put("scene_info", sceneInfo); // 场景信息，当交易类型为MWEB时(即H5支付)，此参数必传
                }
                // 签名
                // 如果参数的值为空不参与签名
                String sign = PayToolUtils.createSign("UTF-8", packageParams, wechat.getAPI());
                packageParams.put("sign", sign); // 签名

                // 将SortedMap转换为xml类型的String
                String requestXML = PayToolUtils.getRequestXml(packageParams);
                // 输出
                logger.error("WechatPayRestful:支付-requestXML:=============================" + requestXML);
                System.out.println("WechatPayRestful:支付-requestXML:=============================" + requestXML);

                // 返回信息
                String responseXml = HttpPayUtils.postData(OAwechatConfig.WECHAT_PAY_URL, requestXML);
                // 输出
                logger.error("WechatPayRestful:支付-responseXml:=============================" + responseXml);
                System.out.println("WechatPayRestful:支付-responseXml:=============================" + responseXml);

                // 获取数据
                Map map = XMLUtil4jdom.doXMLParse(responseXml);
                if ("JSAPI".equals(tradeType)) {
                    // 公众号支付时，获取返回的预支付交易会话标识
                    message = (String) map.get("prepay_id");
                    // 输出
                    logger.error("WechatPayRestful:支付-responseXml:prepay_id:=============================" + message);
                    System.out.println("WechatPayRestful:支付-responseXml:prepay_id:=============================" + message);
                } else if ("MWEB".equals(tradeType)) {
                    // H5支付时，获取返回的支付跳转链接
                    message = (String) map.get("mweb_url");
                    // 输出
                    logger.error("WechatPayRestful:支付-responseXml:code_url:=============================" + message);
                    System.out.println("WechatPayRestful:支付-responseXml:code_url:=============================" + message);
                } else if ("NATIVE".equals(tradeType)) {
                    // 扫码支付时，获取返回的二维码链接
                    message = (String) map.get("code_url");
                    // 输出
                    logger.error("WechatPayRestful:支付-responseXml:code_url:=============================" + message);
                    System.out.println("WechatPayRestful:支付-responseXml:code_url:=============================" + message);
                }
            }

            // 返回数据
            if (message == null || message.isEmpty()) {
                returnMessage.setCount(0);
                returnMessage.setMsg("支付数据获取失败！");
            } else {
                returnMessage.setCount(1);
                returnMessage.setMsg(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMessage;
    }
    /**
     * 字符串转换UTF-8
     * @param string
     * @return
     */
    public static String toUtf8String(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c >= 0 && c <= 255) {
                stringBuffer.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    stringBuffer.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 微信支付
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/pay.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultVO Pay(HttpServletRequest request) {
        ResultVO returnMessage = new ResultVO(); // 返回数据

        String logo = request.getParameter("logo");
        if ("H5".equals(logo)) {
            // 在微信浏览器里面打开H5网页中执行JS调起支付
            // 公众号支付时，根据返回的预支付交易会话标识，获取签名
            String message = "";

            try {
                // 获取微信相关数据
                Wechat wechat = new Wechat();
                List<Wechat>   wechats = wechatService.selectWechatList(wechat);
                 wechat = wechats.get(0);
                if (wechat != null) {
                    // 随机字符串
                    String currTime = PayToolUtils.getCurrTime();
                    String timeStr = currTime.substring(8, currTime.length());
                    String randomStr = PayToolUtils.buildRandom(4) + "";
                    String nonce_str = timeStr + randomStr;

                    // 为微信支付添加参数
                    SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
                    packageParams.put("appId", wechat.getAppId()); // 公众账号ID
                    packageParams.put("timeStamp", "1414587457"); // 时间戳
                    packageParams.put("nonceStr", nonce_str); // 随机字符串
                    packageParams.put("package", "prepay_id=" + request.getParameter("prepayid")); // 公众号支付时，获取返回的预支付交易会话标识
                    packageParams.put("signType", "MD5"); // 签名类型，默认为MD5
                    // 签名
                    // 如果参数的值为空不参与签名
                    String sign = PayToolUtils.createSign("UTF-8", packageParams, wechat.getAPI());
                    packageParams.put("paySign", sign); // 签名

                    // 返回数据
                    message = "appId=" + wechat.getAppId() + "&nonceStr=" + nonce_str + "&paySign=" + sign;
                }

                if (message == null || TextUtils.isEmpty(message)) {
                    returnMessage.setCode(300);
                    returnMessage.setMsg("支付失败！");
                } else {
                    returnMessage.setCode(200);
                    returnMessage.setCount(1);
                    returnMessage.setMsg(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 二维码扫码支付
            // 获取二维码链接
            String tradeType = request.getParameter("tradeType");
            String goodName = request.getParameter("goodName");
            double goodMoney = Double.valueOf(request.getParameter("goodMoney"));
            String openId = request.getParameter("openId");
            String attach = request.getParameter("attach"); // 附加数据，非必填字段，在查询API和支付通知中原样返回，可作为自定义参数使用

            // 微信支付
            String urlCode = wechatPay(wechatService, tradeType, goodName, goodMoney, openId, attach);

            if (urlCode == null || TextUtils.isEmpty(urlCode)) {
                returnMessage.setCode(300);
                returnMessage.setMsg("获取二维码失败！");
            } else {
                returnMessage.setCode(200);
                returnMessage.setMsg(urlCode);
            }
        }

        return returnMessage;
    }

    /**
     * 微信支付
     * <p>
     * 公众号支付时，返回的预支付交易会话标识
     * 扫码支付时，返回二维码链接
     *
     * @param tradeType 交易类型  JSAPI:公众号支付  MWEB:H5支付  NATIVE:扫码支付  APP:APP支付
     * @param goodName  商品名称
     * @param goodMoney 商品价格 double类型，两位小数，例如：1.00
     * @param openId    用户openId
     * @return
     */
    public static String wechatPay(IWechatService wechatService, String tradeType, String goodName, double goodMoney, String openId, String attach) {

        // 返回的数据
        String returnData = "";

        try {
            // 获取微信相关数据
            Wechat wechat = new Wechat();
        List<Wechat>    wechats = wechatService.selectWechatList(wechat);
            wechat = wechats.get(0);
            if (wechat != null) {
                // 随机字符串
                String currTime = PayToolUtils.getCurrTime();
                String timeStr = currTime.substring(8, currTime.length());
                String randomStr = PayToolUtils.buildRandom(4) + "";
                String nonce_str = timeStr + randomStr;
                goodName = toUtf8String(goodName);
                // 商户订单号
                String out_trade_no = "" + System.currentTimeMillis();

                // 商品价格
                // 保留2位小数，四舍五入，并将double转化为Stirng
                String goodMoneyStr = String.valueOf(Math.round(goodMoney * 100));

                // 为微信支付添加参数
                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
                packageParams.put("appid", wechat.getAppId()); // 公众账号ID
                packageParams.put("mch_id", wechat.getMCHID()); // 商户号
                packageParams.put("nonce_str", nonce_str); // 随机字符串
                packageParams.put("body", goodName); // 商品描述
                packageParams.put("attach", attach); // 附加数据，非必填字段，在查询API和支付通知中原样返回，可作为自定义参数使用
                packageParams.put("out_trade_no", out_trade_no); // 商户订单号
                packageParams.put("total_fee", goodMoneyStr); // 价格的单位为分
                packageParams.put("spbill_create_ip", wechat.getPayIp()); // 终端IP
                packageParams.put("notify_url", wechat.getPayNotifyUrl()); // 微信支付回调接口
                packageParams.put("trade_type", tradeType); // 交易类型  JSAPI:公众号支付  MWEB:H5支付  NATIVE:扫码支付  APP:APP支付
                packageParams.put("sign_type", "MD5"); // 签名类型，默认为MD5
                if ("JSAPI".equals(tradeType)) {
                    packageParams.put("openid", openId); // 用户标识，当交易类型为JSAPI时(即公众号支付)，此参数必传
                }
                // 签名
                // 如果参数的值为空不参与签名
                String sign = PayToolUtils.createSign("UTF-8", packageParams, wechat.getAPI());
                packageParams.put("sign", sign); // 签名

                // 将SortedMap转换为xml类型的String
                String requestXML = PayToolUtils.getRequestXml(packageParams);
                // 输出
                System.out.println("WechatPayController:支付-requestXML:=============================" + requestXML);
                System.out.println("WechatPayController:支付-requestXML:=============================" + requestXML);

                // 返回信息
                String responseXml = HttpPayUtils.postData(OAwechatConfig.WECHAT_PAY_URL, requestXML);
                // 输出
                System.out.println("WechatPayController:支付-responseXml:=============================" + responseXml);
                System.out.println("WechatPayController:支付-responseXml:=============================" + responseXml);

                // 获取数据
                Map map = XMLUtil4jdom.doXMLParse(responseXml);
                if ("JSAPI".equals(tradeType)) {
                    // 公众号支付时，获取返回的预支付交易会话标识
                    returnData = (String) map.get("prepay_id");
                    // 输出
                    System.out.println("WechatPayController:支付-responseXml:prepay_id:=============================" + returnData);
                    System.out.println("WechatPayController:支付-responseXml:prepay_id:=============================" + returnData);
                } else if ("MWEB".equals(tradeType)) {
                    // H5支付时，获取返回的支付跳转链接
                    returnData = (String) map.get("mweb_url");
                    // 输出
                    System.out.println("WechatPayRestful:支付-responseXml:code_url:=============================" + returnData);
                    System.out.println("WechatPayRestful:支付-responseXml:code_url:=============================" + returnData);
                } else if ("NATIVE".equals(tradeType)) {
                    // 扫码支付时，获取返回的二维码链接
                    returnData = (String) map.get("code_url");
                    // 输出
                    System.out.println("WechatPayController:支付-responseXml:code_url:=============================" + returnData);
                    System.out.println("WechatPayController:支付-responseXml:code_url:=============================" + returnData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnData;
    }

    /**
     * 公众号支付时，根据返回的预支付交易会话标识，获取签名
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/H5Pay.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String H5Pay(HttpServletRequest request) {
        // 返回的数据
        String message = "";

        try {
            // 获取微信相关数据
            Wechat wechat = new Wechat();
         List<Wechat>   wechats = wechatService.selectWechatList(wechat);
            wechat = wechats.get(0);
            if (wechat != null) {
                // 随机字符串
                String currTime = PayToolUtils.getCurrTime();
                String timeStr = currTime.substring(8, currTime.length());
                String randomStr = PayToolUtils.buildRandom(4) + "";
                String nonce_str = timeStr + randomStr;
                // 为微信支付添加参数
                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
                packageParams.put("appId", wechat.getAppId()); // 公众账号ID
                packageParams.put("timeStamp", "1414587457"); // 时间戳
                packageParams.put("nonceStr", nonce_str); // 随机字符串
                packageParams.put("package", "prepay_id=" + request.getParameter("prepayId")); // 公众号支付时，获取返回的预支付交易会话标识
                packageParams.put("signType", "MD5"); // 签名类型，默认为MD5
                // 签名
                // 如果参数的值为空不参与签名
                String sign = PayToolUtils.createSign("UTF-8", packageParams, wechat.getAPI());
                packageParams.put("paySign", sign); // 签名

                message = "appId=" + wechat.getAppId() + "&nonceStr=" + nonce_str +
                        "&package=" + "prepay_id=" + request.getParameter("prepayId") + "&paySign=" + sign;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回数据
        return message;
    }

    /**
     * 显示二维码图片
     *
     * @param request
     * @param response
     * @param modelMap
     */
    @RequestMapping(value = "/QRCode.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void qrcode(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        try {
            int width = 300; // 二维码图片的宽
            int height = 300; // 二维码图片的高
            String text = request.getParameter("text"); // 二维码图片的内容
            String format = "gif"; // 二维码的图片格式

            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用编码

            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            BuildQRCode.writeToStream(bitMatrix, format, response.getOutputStream());
        } catch (Exception e) {

        }
    }

    /**
     * 微信支付回调接口
     * 扫码支付回调接口
     * (公众号支付没有回调这里，而是直接在界面中进行的数据操作)
     * (扫码支付成功回调这里)
     *
     * @param request
     * @param response
     * @throws JDOMException
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/notify.action")
    public String Notify(HttpServletRequest request, HttpServletResponse response) throws JDOMException, Exception {
        String returnUrl = "zhifujieguo.html?state=0"; // 默认返回页面
        // 输出
        logger.error("WechatPayController:支付回调=============================进入回调接口");
        System.out.println("WechatPayController:支付回调=============================进入回调接口");
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
        logger.error("WechatPayController:支付回调-packageParams:=============================" + packageParams);
        System.out.println("WechatPayController:支付回调-packageParams:=============================" + packageParams);
        // 获取附加数据中传递过来的业务数据
        String attach = (String) packageParams.get("attach");
        int front = attach.indexOf(","); // 获取分隔符的位置
        String workType = attach.substring(0, front);  // 业务类型
        // 业务数据
        String biddingType = null, userid = null, orderid = null, amountString = null;
        attach = attach.substring(front + 1);
        int front1 = attach.indexOf(","); // 获取分隔符的位置
        biddingType = attach.substring(0, front1);  //第一个参数
        // 用户id
        attach = attach.substring(front1 + 1);
        int front2 = attach.indexOf(","); // 获取分隔符的位置
        userid = attach.substring(0, front2);//第二个参数
        // 数据id
        attach = attach.substring(front2 + 1);
        int front3 = attach.indexOf(","); // 获取分隔符的位置
        orderid = attach.substring(0, front3);  //第三个参数  订单id
        /**
         * 获取订单ID
         * 获取订单ID
         * 获取订单ID
         */
        // 金额
        attach = attach.substring(front3 + 1);
        amountString = attach.substring(0, attach.length());
        // 输出
//         	logger.error("WechatPayController:支付回调-attach:=============================" + attach);
        logger.error("WechatPayController:支付回调-业务中的竞价排名类型:=============================" + biddingType);
        logger.error("WechatPayController:支付回调-业务中的用户用户id:=============================" + userid);
        logger.error("WechatPayController:支付回调-业务中的数据订单ID:=============================" + orderid);
        logger.error("WechatPayController:支付回调-业务中的金额:=============================" + amountString);
//         	System.out.println("WechatPayController:支付回调-attach:=============================" + attach);
        System.out.println("WechatPayController:支付回调-业务中的竞价排名类型:=============================" + biddingType);
        System.out.println("WechatPayController:支付回调-业务中的用户id:=============================" + userid);
        System.out.println("WechatPayController:支付回调-业务中的数据订单ID:=============================" + orderid);
        System.out.println("WechatPayController:支付回调-业务中的金额:=============================" + amountString);

        // 获取微信相关数据
        Wechat wechat = new Wechat();
      List<Wechat>  wechats = wechatService.selectWechatList(wechat);
        wechat = wechats.get(0);
        if (wechat != null) {
            // 根据公众账号ID，判断签名是否正确
            if (PayToolUtils.isTenpaySign("UTF-8", packageParams, wechat.getAPI())) {
                // 输出
                logger.error("WechatPayController:支付回调=============================签名验证正确，处理业务开始");
                System.out.println("WechatPayController:支付回调=============================签名验证正确，处理业务开始");
                // 处理业务开始
                String responseXml = "";  // 通知微信接收回调成功
                if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                    // 输出
                    logger.error("WechatPayController:支付回调=============================支付成功");
                    System.out.println("WechatPayController:支付回调=============================支付成功");
                    // 支付成功
                    // 通知微信，异步确认成功
                    // 必写，不然会一直通知后台，八次之后就认为交易失败
                    responseXml =
                            "<xml>" +
                                    "<return_code><![CDATA[SUCCESS]]></return_code>" +
                                    "<return_msg><![CDATA[OK]]></return_msg>" +
                                    "</xml> ";

                    // 执行自己的业务逻辑
                    int num = 1;
                    /**
                     *
                     *
                     * 执行自己的业务逻辑
                     */

                    /**
                     *
                     *
                     * 执行自己的业务逻辑
                     */
                    // 执行自己的业务逻辑
                    // 跳转页面
                    if (num > 0) {
                        returnUrl = "zhifujieguo.html?state=1";
                    }
                } else {
                    // 输出
                    logger.error("WechatPayController:支付回调=============================支付失败");
                    System.out.println("WechatPayController:支付回调=============================支付失败");

                    responseXml =
                            "<xml>" +
                                    "<return_code><![CDATA[FAIL]]></return_code>" +
                                    "<return_msg><![CDATA[报文为空]]></return_msg>" +
                                    "</xml> ";
                }

                // 通知微信接收回调成功
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(responseXml.getBytes());
                out.flush();
                out.close();

                // 处理业务完毕
                // 输出
                logger.error("WechatPayController:支付回调=============================签名验证正确，处理业务结束");
                System.out.println("WechatPayController:支付回调=============================签名验证正确，处理业务结束");
            } else {
                // 输出
                logger.error("WechatPayController:支付回调=============================签名验证失败");
                System.out.println("WechatPayController:支付回调=============================签名验证失败");
            }
        }

        // 跳转页面
        return returnUrl;
    }

}
