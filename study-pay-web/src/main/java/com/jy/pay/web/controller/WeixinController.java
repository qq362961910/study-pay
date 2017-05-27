package com.jy.pay.web.controller;


import com.jy.pay.common.util.DigestUtil;
import com.jy.pay.common.util.RandomUtil;
import com.jy.pay.entity.weixin.BaseWeixinMessage;
import com.jy.pay.entity.weixin.enums.MessageType;
import com.jy.pay.weixin.aes.WXBizMsgCrypt;
import com.jy.pay.weixin.handler.MessageHandler;
import com.jy.pay.weixin.handler.MessageHandlerManager;
import com.jy.pay.weixin.helper.WeixinHelper;
import com.jy.pay.weixin.helper.config.WeixinPayConfigure;
import com.jy.pay.weixin.producer.MessageProducer;
import com.jy.pay.weixin.producer.MessageProducerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
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
    private static final XMLOutputter outputter = new XMLOutputter();

    @Autowired
    private WeixinHelper weixinHelper;
    @Autowired
    private WeixinPayConfigure weixinPayConfigure;
    @Autowired
    private MessageHandlerManager messageHandlerManager;
    @Autowired
    private MessageProducerManager messageProducerManager;
    @Autowired
    private WXBizMsgCrypt crypter;

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
            if (sb.length() > 0) {
                String originalRequestBody = sb.toString();
                logger.debug("original request body: \r\n" + originalRequestBody);

                String requestMessage;
                if (encryptType!= null) {
                    WXBizMsgCrypt crypter = new WXBizMsgCrypt(weixinPayConfigure.getToken(), weixinPayConfigure.getAesKey(), weixinPayConfigure.getAppID());
                    requestMessage = crypter.decryptMsg(messageSignature, timestamp, nonce, originalRequestBody);
                    logger.info("message: \r\n" + requestMessage);
                }
                else {
                    requestMessage = originalRequestBody;
                }

                SAXBuilder saxBuilder = new SAXBuilder();
                Document requestMessageXml = saxBuilder.build(new StringReader(requestMessage));
                Element root = requestMessageXml.getRootElement();
                String toUserName = root.getChild("ToUserName").getText();
                String fromUserName = root.getChild("FromUserName").getText();
                String createTime = root.getChild("CreateTime").getText();
                String messageType = root.getChild("MsgType").getText();

                logger.info("message body: \r\n");
                logger.info("toUserName: " + toUserName);
                logger.info("fromUserName: " + fromUserName);
                logger.info("createTime: " + createTime);
                logger.info("messageType: " + messageType);

                MessageType type = MessageType.getMessageType(messageType);
                logger.info("receive a message: " + type);
                MessageHandler messageHandler = messageHandlerManager.getMessageHandler(type);
                if (messageHandler != null) {
                    MessageProducer producer = messageProducerManager.getMessageProducer(type);
                    if (producer != null) {
                        BaseWeixinMessage message = producer.produce(root);
                        if (message != null) {
                            Object result = messageHandler.handle(message);
                            if (request != null) {
                                if (encryptType!= null) {
                                    return crypter.encryptMsg(outputter.outputString((Document)result), timestamp, RandomUtil.getRandomStr());
                                }
                                else {
                                    return outputter.outputString((Document)result);
                                }
                            }
                        }
                    }
                    else {
                        logger.warn("no message producer found for message type: " + messageType);
                    }
                }
                else {
                    logger.warn("no message handler found for message type: " + messageType);
                }
            }
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
        logger.info("redirect for code, url: \r\n" + redirectUrl);
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
