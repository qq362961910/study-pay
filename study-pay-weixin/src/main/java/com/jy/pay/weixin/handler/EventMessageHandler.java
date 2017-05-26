package com.jy.pay.weixin.handler;

import com.jy.pay.entity.weixin.BaseWeixinMessage;
import com.jy.pay.entity.weixin.enums.MessageType;
import org.springframework.stereotype.Component;

/**
 * 事件消息处理器
 */
@Component
public class EventMessageHandler implements MessageHandler{

    @Override
    public boolean support(MessageType messageType) {
        return MessageType.EVENT == messageType;
    }

    @Override
    public Object handle(BaseWeixinMessage weixinMessage) throws Exception {
        return null;
    }
}
