package com.jy.pay.weixin.helper.config;

public class WeixinPayConfigure {

    //key
    private  String key = "nDJ52Itr3s2EiDSzL2fsZ657gSI4g40c";
    //微信分配的公众号ID（开通公众号之后可以获取到）
    private  String appID = "wx22972e1527274740";
    //secret
    private  String secret = "2ee86ef088543b194c77be06de761566";
    //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    private  String mchID = "1451278902";
    //受理模式下给子商户分配的子商户号
    private  String subMchID = "";
    //HTTPS证书的本地路径
    private  String certLocalPath = "";
    //HTTPS证书密码，默认密码等于商户号MCHID
    private  String certPassword = "";
    //是否使用异步线程的方式来上报API测速，默认为异步模式
    private  boolean useThreadToDoReport = true;
    //机器IP
    private  String ip = "";
    //以下是几个API的路径：
    //1）被扫支付API
    public  String payApi = "https://api.mch.weixin.qq.com/pay/micropay";
    //2）被扫支付查询API
    public  String payQueryApi = "https://api.mch.weixin.qq.com/pay/orderquery";
    //3）退款API
    public  String refundApi = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //4）退款查询API
    public  String refundQueryApi = "https://api.mch.weixin.qq.com/pay/refundquery";
    //5）撤销API
    public  String reverseApi = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    //6）下载对账单API
    public  String downloadBillApi = "https://api.mch.weixin.qq.com/pay/downloadbill";
    //7) 统计上报API
    public  String reportApi = "https://api.mch.weixin.qq.com/payitil/report";
    //(8) access token url
    public  String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    //(9) auth call back url
    public  String callBackHost = "99pay.51play.com";
    //(10) app access token
    public String appAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";


    public String getKey() {
        return key;
    }

    public WeixinPayConfigure setKey(String key) {
        this.key = key;
        return this;
    }

    public String getAppID() {
        return appID;
    }

    public WeixinPayConfigure setAppID(String appID) {
        this.appID = appID;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public WeixinPayConfigure setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getMchID() {
        return mchID;
    }

    public WeixinPayConfigure setMchID(String mchID) {
        this.mchID = mchID;
        return this;
    }

    public String getSubMchID() {
        return subMchID;
    }

    public WeixinPayConfigure setSubMchID(String subMchID) {
        this.subMchID = subMchID;
        return this;
    }

    public String getCertLocalPath() {
        return certLocalPath;
    }

    public WeixinPayConfigure setCertLocalPath(String certLocalPath) {
        this.certLocalPath = certLocalPath;
        return this;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public WeixinPayConfigure setCertPassword(String certPassword) {
        this.certPassword = certPassword;
        return this;
    }

    public boolean isUseThreadToDoReport() {
        return useThreadToDoReport;
    }

    public WeixinPayConfigure setUseThreadToDoReport(boolean useThreadToDoReport) {
        this.useThreadToDoReport = useThreadToDoReport;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public WeixinPayConfigure setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getPayApi() {
        return payApi;
    }

    public WeixinPayConfigure setPayApi(String payApi) {
        this.payApi = payApi;
        return this;
    }

    public String getPayQueryApi() {
        return payQueryApi;
    }

    public WeixinPayConfigure setPayQueryApi(String payQueryApi) {
        this.payQueryApi = payQueryApi;
        return this;
    }

    public String getRefundApi() {
        return refundApi;
    }

    public WeixinPayConfigure setRefundApi(String refundApi) {
        this.refundApi = refundApi;
        return this;
    }

    public String getRefundQueryApi() {
        return refundQueryApi;
    }

    public WeixinPayConfigure setRefundQueryApi(String refundQueryApi) {
        this.refundQueryApi = refundQueryApi;
        return this;
    }

    public String getReverseApi() {
        return reverseApi;
    }

    public WeixinPayConfigure setReverseApi(String reverseApi) {
        this.reverseApi = reverseApi;
        return this;
    }

    public String getDownloadBillApi() {
        return downloadBillApi;
    }

    public WeixinPayConfigure setDownloadBillApi(String downloadBillApi) {
        this.downloadBillApi = downloadBillApi;
        return this;
    }

    public String getReportApi() {
        return reportApi;
    }

    public WeixinPayConfigure setReportApi(String reportApi) {
        this.reportApi = reportApi;
        return this;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public WeixinPayConfigure setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
        return this;
    }

    public String getCallBackHost() {
        return callBackHost;
    }

    public WeixinPayConfigure setCallBackHost(String callBackHost) {
        this.callBackHost = callBackHost;
        return this;
    }

    public String getAppAccessTokenUrl() {
        return appAccessTokenUrl;
    }

    public void setAppAccessTokenUrl(String appAccessTokenUrl) {
        this.appAccessTokenUrl = appAccessTokenUrl;
    }
}
