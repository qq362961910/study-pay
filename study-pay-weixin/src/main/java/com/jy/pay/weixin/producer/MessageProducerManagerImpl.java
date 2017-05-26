package com.jy.pay.weixin.producer;

import com.jy.pay.entity.weixin.enums.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageProducerManagerImpl implements MessageProducerManager {

    @Autowired
    private List<MessageProducer> messageProducerList;

    @Override
    public MessageProducer getMessageProducer(MessageType messageType) {
        for (MessageProducer producer: messageProducerList) {
            if (producer.support(messageType)) {
                return producer;
            }
        }
        return null;
    }
}
