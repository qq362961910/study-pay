package com.jy.pay.weixin.handler;

import com.jy.pay.common.util.RandomUtil;
import com.jy.pay.entity.weixin.BaseWeixinMessage;
import com.jy.pay.entity.weixin.TextMessage;
import com.jy.pay.entity.weixin.enums.MessageType;
import com.jy.pay.weixin.aes.AesException;
import com.jy.pay.weixin.aes.WXBizMsgCrypt;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文本消息处理器
 */
@Component
public class TextMessageHandler implements MessageHandler {

    @Autowired
    private WXBizMsgCrypt crypter;
    private XMLOutputter outputter = new XMLOutputter();

    @Override
    public boolean support(MessageType messageType) {
        return MessageType.TEXT == messageType;
    }

    @Override
    public Object handle(BaseWeixinMessage weixinMessage) throws Exception{

        //echo handler
        Element responseXml = new Element("xml");
        Document responseDoc = new Document(responseXml);

        Element toUserNameE = new Element("ToUserName");
        toUserNameE.setContent(new CDATA(weixinMessage.getFromUserName()));
        responseXml.addContent(toUserNameE);

        Element fromUserNameE = new Element("FromUserName");
        fromUserNameE.setContent(new CDATA(weixinMessage.getToUserName()));
        responseXml.addContent(fromUserNameE);

        Element createTimeE = new Element("CreateTime");
        String timestamp = String.valueOf(System.currentTimeMillis());
        createTimeE.setText(timestamp);
        responseXml.addContent(createTimeE);

        Element messageTypeE = new Element("MsgType");
        messageTypeE.setText(MessageType.TEXT.getValue());
        responseXml.addContent(messageTypeE);

        Element contentE = new Element("Content");
        contentE.setContent(new CDATA(((TextMessage)weixinMessage).getContent()));
        responseXml.addContent(contentE);

        return crypter.encryptMsg(outputter.outputString(responseDoc), timestamp, RandomUtil.getRandomStr());
    }
}
