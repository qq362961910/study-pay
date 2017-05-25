package com.jy.pay.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {

    private static final Logger logger = LogManager.getLogger(DigestUtil.class);

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";

    public static MessageDigest getMD5() {
        try {
            return MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            logger.error("get MD5 instance error", e);
            return null;
        }
    }

    public static MessageDigest getSha1() {
        try {
            return MessageDigest.getInstance(SHA1);
        } catch (NoSuchAlgorithmException e) {
            logger.error("get SHA1 instance error", e);
            return null;
        }
    }

}
