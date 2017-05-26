package com.jy.pay.entity.weixin;

/**
 * 语音消息
 */
public class VoiceMessage extends MediaMessage {

    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
