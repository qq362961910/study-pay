package com.jy.pay.entity.weixin;

/**
 * 小视频消息
 */
public class ShortVideoMessage extends MediaMessage {

    private String thumbMediaId;

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
