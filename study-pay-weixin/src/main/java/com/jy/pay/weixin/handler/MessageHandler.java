package com.jy.pay.weixin.handler;

import com.jy.pay.entity.weixin.BaseWeixinMessage;
import com.jy.pay.entity.weixin.enums.MessageType;
import com.jy.pay.weixin.aes.AesException;

/**
 * 消息处理器
 */
public interface MessageHandler {

    boolean support(MessageType messageType);

    Object handle(BaseWeixinMessage weixinMessage) throws Exception;

}
