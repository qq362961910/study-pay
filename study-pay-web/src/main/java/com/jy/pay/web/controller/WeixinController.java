package com.jy.pay.web.controller;


import com.jy.pay.common.util.DigestUtil;
import com.jy.pay.entity.weixin.TextMessage;
import com.jy.pay.weixin.aes.AesException;
import com.jy.pay.weixin.aes.WXBizMsgCrypt;
import com.jy.pay.weixin.handler.TextMessageHandler;
import com.jy.pay.weixin.helper.WeixinHelper;
import com.jy.pay.weixin.helper.config.WeixinPayConfigure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.CDATA;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
    @Autowired
    private TextMessageHandler textMessageHandler;

    @RequestMapping
    public Object getCallback(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
        logger.info("receive param: " + params);
        Object signature = params.get("signature");
        String echostr = params.get("echostr");
        String timestamp = params.get("timestamp");
        String nonce = params.get("nonce");
        String openid = params.get("openid");

        String encryptType = params.get("encrypt_type");
        String messageSignature = params.get("msg_signature");

        List<String> elements = new ArrayList<>();
        elements.add(timestamp);
        elements.add(nonce);
        elements.add(weixinPayConfigure.getToken());
        Collections.sort(elements);
        StringBuilder sb = new StringBuilder();
        for (String str: elements) {
            sb.append(str);
        }
        byte[] digestBytes = DigestUtil.getSha1().digest(sb.toString().getBytes());
        String sign = DatatypeConverter.printHexBinary(digestBytes).toLowerCase();
        InputStream is = request.getInputStream();

        //接受xml消息，并解密消息
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

            String originalRequestBody = sb.toString();
            logger.debug("original request body: \r" + originalRequestBody);


            WXBizMsgCrypt crypter = new WXBizMsgCrypt(weixinPayConfigure.getToken(), weixinPayConfigure.getAesKey(), weixinPayConfigure.getAppID());
            String requestMessage = crypter.decryptMsg(messageSignature, timestamp, nonce, originalRequestBody);
            logger.info("message: \r" + requestMessage);

            XMLOutputter outputter = new XMLOutputter();
            SAXBuilder saxBuilder = new SAXBuilder();
            Document requestMessageXml = saxBuilder.build(new StringReader(requestMessage));
            Element root = requestMessageXml.getRootElement();
            String toUserName = root.getChild("ToUserName").getText();
            String fromUserName = root.getChild("FromUserName").getText();
            String createTime = root.getChild("CreateTime").getText();
            String messageType = root.getChild("MsgType").getText();
            String content = root.getChild("Content").getText();
            String msgId = root.getChild("MsgId").getText();

            logger.info("message body: \r");
            logger.info("toUserName: " + toUserName);
            logger.info("fromUserName: " + fromUserName);
            logger.info("createTime: " + createTime);
            logger.info("messageType: " + messageType);
            logger.info("content: " + content);
            logger.info("msgId: " + msgId);

            //echo handler
            TextMessage textMessage = new TextMessage();
            textMessage.setFromUserName(fromUserName);
            textMessage.setToUserName(toUserName);
            textMessage.setCrateTime(createTime);
            textMessage.setMessageId(msgId);
            textMessage.setContent(content);
            return textMessageHandler.handle(textMessage);

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
