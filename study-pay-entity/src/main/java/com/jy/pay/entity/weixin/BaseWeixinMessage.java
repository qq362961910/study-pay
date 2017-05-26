package com.jy.pay.entity.weixin;

import com.jy.pay.entity.weixin.enums.MessageType;

/**
 * 微信基础类
 */
public class BaseWeixinMessage {

    private String toUserName;

    private String fromUserName;

    private String crateTime;

    private MessageType messageType;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(String crateTime) {
        this.crateTime = crateTime;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

}
