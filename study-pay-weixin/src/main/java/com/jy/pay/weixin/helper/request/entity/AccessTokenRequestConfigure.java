package com.jy.pay.weixin.helper.request.entity;


import com.jy.pay.common.anno.UrlParam;

public class AccessTokenRequestConfigure {

    @UrlParam("appid")
    private String appid;
    @UrlParam("secret")
    private String secret;
    @UrlParam("code")
    private String code;
    @UrlParam("grant_type")
    private String grantType = "authorization_code";

    public String getAppid() {
        return appid;
    }

    public AccessTokenRequestConfigure setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public AccessTokenRequestConfigure setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AccessTokenRequestConfigure setCode(String code) {
        this.code = code;
        return this;
    }

    public String getGrantType() {
        return grantType;
    }

    public AccessTokenRequestConfigure setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public static AccessTokenRequestConfigure defaultConfig(String authCode) {
        AccessTokenRequestConfigure configure = new AccessTokenRequestConfigure();
        configure.appid = configure.getAppid();
        configure.secret = configure.getSecret();
        configure.code = authCode;
        return configure;
    }
}
