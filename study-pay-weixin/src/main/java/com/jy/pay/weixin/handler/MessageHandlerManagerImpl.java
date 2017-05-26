package com.jy.pay.weixin.handler;

import com.jy.pay.entity.weixin.enums.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息处理器管理
 */
@Component
public class MessageHandlerManagerImpl implements MessageHandlerManager{

    @Autowired
    private List<MessageHandler> messageHandlerList;

    @Override
    public MessageHandler getMessageHandler(MessageType messageType) {
        for (MessageHandler handler: messageHandlerList) {
            if (handler.support(messageType)) {
                return handler;
            }
        }
        return null;
    }
}
