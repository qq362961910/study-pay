package com.jy.pay.web.controller;


import com.jy.pay.common.util.DigestUtil;
import com.jy.pay.weixin.aes.AesException;
import com.jy.pay.weixin.aes.WXBizMsgCrypt;
import com.jy.pay.weixin.helper.WeixinHelper;
import com.jy.pay.weixin.helper.config.WeixinPayConfigure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.SourceType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/weixin")
@RestController
public class WeixinController extends BaseController{

    private final Logger logger = LogManager.getLogger(WeixinController.class);

    @Autowired
    private WeixinHelper weixinHelper;
    @Autowired
    private WeixinPayConfigure weixinPayConfigure;

    @RequestMapping
    public Object callback(@RequestParam Map<String, String> params, HttpServletRequest request) throws IOException, JDOMException, AesException {
        logger.info("receive param: " + params);
        Object signature = params.get("signature");
        String echostr = params.get("echostr");
        String timestamp = params.get("timestamp");
        String nonce = params.get("nonce");
        String openid = params.get("openid");
        String token = "123456";

        String encryptType = params.get("encrypt_type");
        String messageSignature = params.get("msg_signature");

        List<String> elements = new ArrayList<>();
        elements.add(timestamp);
        elements.add(nonce);
        elements.add(token);
        Collections.sort(elements);
        StringBuilder sb = new StringBuilder();
        for (String str: elements) {
            sb.append(str);
        }
        byte[] digestBytes = DigestUtil.getSha1().digest(sb.toString().getBytes());
        String sign = DatatypeConverter.printHexBinary(digestBytes).toLowerCase();
        InputStream is = request.getInputStream();
        if (is != null) {
            sb.setLength(0);
            byte[] buf = new byte[1024];
            while(true) {
                int len = is.read(buf);
                if (len == -1) {
                    break;
                }
                sb.append(new String(buf, 0, len));
            }
            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(new StringReader(sb.toString()));
            XMLOutputter outputter = new XMLOutputter();
            String requestBody = outputter.outputString(doc);
            logger.debug("content: \r" + requestBody);

            WXBizMsgCrypt crypt = new WXBizMsgCrypt(weixinPayConfigure.getToken(), weixinPayConfigure.getAesKey(), weixinPayConfigure.getAppID());
            String message = crypt.decryptMsg(messageSignature, timestamp, nonce, requestBody);
            logger.debug("message: \r" + message);

            Document messageXml = saxBuilder.build(new StringReader(message));

            Element root = messageXml.getRootElement();
            String fromUserName = root.getChild("FromUserName").getText();
            String createTime = root.getChild("CreateTime").getText();
            String messageType = root.getChild("MsgType").getText();
            String content = root.getChild("Content").getText();
            String msgId = root.getChild("MsgId").getText();

            logger.info("message body: \r");
            logger.info("fromUserName: " + fromUserName);
            logger.info("createTime: " + createTime);
            logger.info("messageType: " + messageType);
            logger.info("content: " + content);
            logger.info("msgId: " + msgId);
            logger.info("openid: " + openid);
        }
        if (signature.equals(sign)) {
            logger.info("verify signature success");
            return echostr;
        }
        logger.error("verify signature fail");
        return fail();
    }

    /**
     * 获取App AccessToken
     * */
    @RequestMapping(value = "/app/accessToken", method = RequestMethod.GET)
    public Object getAppAccessToken() throws IOException, IllegalAccessException {
        Map<String, Object> result = weixinHelper.requestAppAccessToken();
        return success(result);
    }

    /**
     * 用户重定向获取Code
     * */
    @RequestMapping("/redirect/code")
    public void redirectForCode(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String callback = "http://study-pay.nat123.net/weixin/callback/code";
        String callbackToUse = URLEncoder.encode(callback, "UTF-8");
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + weixinPayConfigure.getAppID() + "&redirect_uri=" + callbackToUse + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        response.sendRedirect(redirectUrl);
    }

    /**
     * 用户重定向获取Code回调
     * */
    @RequestMapping("/callback/code")
    public void callbackCode(@RequestParam Map<String, String> params) {
        logger.info(params);
    }

}
