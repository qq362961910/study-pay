package com.jy.pay.weixin.producer;

import com.jy.pay.entity.weixin.BaseWeixinMessage;
import com.jy.pay.entity.weixin.TextMessage;
import com.jy.pay.entity.weixin.enums.MessageType;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * 文本消息生成器
 */
@Component
public class TextMessageProducer implements MessageProducer{

    @Override
    public boolean support(MessageType messageType) {
        return MessageType.TEXT == messageType;
    }

    @Override
    public BaseWeixinMessage produce(Element root) throws Exception {
        String toUserName = root.getChild("ToUserName").getText();
        String fromUserName = root.getChild("FromUserName").getText();
        String createTime = root.getChild("CreateTime").getText();
        String content = root.getChild("Content").getText();
        String msgId = root.getChild("MsgId").getText();
        TextMessage textMessage = new TextMessage();
        textMessage.setFromUserName(fromUserName);
        textMessage.setToUserName(toUserName);
        textMessage.setCrateTime(createTime);
        textMessage.setMessageId(msgId);
        textMessage.setContent(content);
        return textMessage;
    }
}
