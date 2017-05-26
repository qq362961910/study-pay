package com.jy.pay.weixin.producer;

import com.jy.pay.entity.weixin.BaseWeixinMessage;
import com.jy.pay.entity.weixin.enums.MessageType;
import org.jdom2.Element;

public interface MessageProducer {

    boolean support(MessageType messageType);

    BaseWeixinMessage produce(Element root) throws Exception;

}
