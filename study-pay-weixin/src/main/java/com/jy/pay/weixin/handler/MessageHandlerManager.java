package com.jy.pay.weixin.handler;

import com.jy.pay.entity.weixin.enums.MessageType;

public interface MessageHandlerManager {

    MessageHandler getMessageHandler(MessageType messageType);
}
