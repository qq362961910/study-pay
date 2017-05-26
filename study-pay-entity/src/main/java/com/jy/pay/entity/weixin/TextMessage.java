package com.jy.pay.entity.weixin;

import com.jy.pay.entity.weixin.enums.MessageType;

/**
 * 文本消息
 */
public class TextMessage extends BaseWeixinMessage {

    /**
     * 消息内容
     * */
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextMessage () {
        super.setMessageType(MessageType.TEXT);
    }
}
