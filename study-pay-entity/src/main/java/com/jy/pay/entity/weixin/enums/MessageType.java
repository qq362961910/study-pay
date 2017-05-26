package com.jy.pay.entity.weixin.enums;

/**
 * 消息类型
 * */
public enum MessageType {

    TEXT("text", "文本消息"),
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"),
    VIDEO("video", "视频消息"),
    SHORT_VIDEO("shortvideo", "小视频消息"),
    LOCATION("location", "地理位置消息"),
    LINK("link", "链接消息"),
    EVENT("event", "事件")
    ;

    public static MessageType getMessageType(String value) {
        for (MessageType type: values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    MessageType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
