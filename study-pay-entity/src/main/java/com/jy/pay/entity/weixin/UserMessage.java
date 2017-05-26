package com.jy.pay.entity.weixin;

public class UserMessage extends BaseWeixinMessage {

    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
