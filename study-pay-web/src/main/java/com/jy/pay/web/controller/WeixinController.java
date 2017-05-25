package com.jy.pay.web.controller;


import com.jy.pay.common.util.DigestUtil;
import com.jy.pay.weixin.helper.WeixinHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/weixin")
@RestController
public class WeixinController extends BaseController{

    private final Logger logger = LogManager.getLogger(WeixinController.class);

    @Autowired
    private WeixinHelper weixinHelper;

    @RequestMapping("/token/verify")
    public Object tokenVerify(@RequestParam Map<String, String> params) {
        logger.info("receive param: " + params);
        //{signature=7c529d11813bf9caa8d2e10f5d20eada955a4b84, echostr=8267344574111718271, timestamp=1495702395, nonce=2053745369}
        Object signature = params.get("signature");
        String echostr = params.get("echostr");
        params.remove("signature");
        params.remove("echostr");
        //前端网页指定的token
        String token = "123456";
        List<String> elements = new ArrayList<String>();
        elements.add(token);
        for (String value: params.values()) {
            elements.add(value);
        }
        Collections.sort(elements);
        StringBuilder sb = new StringBuilder();
        for (String str: elements) {
            sb.append(str);
        }
        byte[] digestBytes = DigestUtil.getSha1().digest(sb.toString().getBytes());
        String sign = DatatypeConverter.printHexBinary(digestBytes).toLowerCase();
        if (signature.equals(sign)) {
            return echostr;
        }
        return fail();
    }
}
