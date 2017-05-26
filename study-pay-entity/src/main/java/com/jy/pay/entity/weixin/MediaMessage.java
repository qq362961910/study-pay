package com.jy.pay.entity.weixin;

/**
 * 媒体消息
 */
public class MediaMessage extends UserMessage {

    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
