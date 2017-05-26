package com.jy.pay.entity.weixin;

/**
 * 图片消息
 */
public class ImageMessage extends MediaMessage {

    private String pictureUrl;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

}
