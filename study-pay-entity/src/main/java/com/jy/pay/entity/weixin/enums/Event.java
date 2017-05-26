package com.jy.pay.entity.weixin.enums;

/**
 * 事件
 */
public enum Event {

    SUBSCRIBE("subscribe", "订阅"),
    UNSUBSCRIBE("unsubscribe", "取消订阅"),
    SCAN("SCAN", "扫描"),
    LOCATION("LOCATION", "上传地理位置"),
    CLICK("CLICK", "跟菜单点击"),
    VIEW("VIEW", "点击掉转链接")
    ;

    public static Event getEvent(String value) {
        for (Event event: values()) {
            if (event.value.equals(value)) {
                return event;
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

    Event(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Event{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
