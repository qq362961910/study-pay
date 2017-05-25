package com.jy.pay.weixin.helper.request.entity;


import com.jy.pay.common.anno.XmlElement;
import com.jy.pay.common.anno.XmlRootElement;
import com.jy.pay.weixin.helper.config.Configure;

@XmlRootElement("xml")
public class UnionPayRequestConfigure {

    @XmlElement("appid")
    private String appid;
    @XmlElement("attach")
    private String attach;
    @XmlElement("body")
    private String body;
    @XmlElement("mch_id")
    private String mchId;
    @XmlElement(value = "detail", escape = true)
    private String detail;
    @XmlElement("nonce_str")
    private String nonceStr;
    @XmlElement("notify_url")
    private String notifyUrl;
    @XmlElement("openid")
    private String openid;
    @XmlElement("out_trade_no")
    private String outTradeNo;
    @XmlElement("spbill_create_ip")
    private String spbillCreateIp;
    @XmlElement("total_fee")
    private String totalFee;
    @XmlElement("trade_type")
    private String tradeType;
    @XmlElement("sign_type")
    private String signType;
    @XmlElement("sign")
    private String sign;

    public static UnionPayRequestConfigure defaultUnionPayRequestConfigure() {
        UnionPayRequestConfigure unionPayRequestConfigure = new UnionPayRequestConfigure();
        unionPayRequestConfigure.appid = Configure.getAppid();
        unionPayRequestConfigure.mchId = Configure.getMchid();
        return unionPayRequestConfigure;
    }

    public String getAppid() {
        return appid;
    }

    public UnionPayRequestConfigure setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getAttach() {
        return attach;
    }

    public UnionPayRequestConfigure setAttach(String attach) {
        this.attach = attach;
        return this;
    }

    public String getBody() {
        return body;
    }

    public UnionPayRequestConfigure setBody(String body) {
        this.body = body;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public UnionPayRequestConfigure setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public UnionPayRequestConfigure setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public UnionPayRequestConfigure setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public UnionPayRequestConfigure setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public UnionPayRequestConfigure setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public UnionPayRequestConfigure setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public UnionPayRequestConfigure setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
        return this;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public UnionPayRequestConfigure setTotalFee(String totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public UnionPayRequestConfigure setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getSignType() {
        return signType;
    }

    public UnionPayRequestConfigure setSignType(String signType) {
        this.signType = signType;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public UnionPayRequestConfigure setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
