package com.jy.pay.weixin.helper.request.entity;

import com.jy.pay.common.anno.UrlParam;

public class AppAccessTokenRequestConfigure {

    @UrlParam("appid")
    private String appid;
    @UrlParam("secret")
    private String secret;
    @UrlParam("grant_type")
    private String grantType = "client_credential";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public static AppAccessTokenRequestConfigure defaultConfig(String appid, String secret) {
        AppAccessTokenRequestConfigure configure = new AppAccessTokenRequestConfigure();
        configure.appid = appid;
        configure.secret = secret;
        return configure;
    }
}
