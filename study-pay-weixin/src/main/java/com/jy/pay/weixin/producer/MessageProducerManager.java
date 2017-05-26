package com.jy.pay.weixin.producer;

import com.jy.pay.entity.weixin.enums.MessageType;

public interface MessageProducerManager {
    MessageProducer getMessageProducer(MessageType messageType);
}
