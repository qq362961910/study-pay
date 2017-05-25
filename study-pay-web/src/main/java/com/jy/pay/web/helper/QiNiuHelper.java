package com.jy.pay.web.helper;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class QiNiuHelper {


    public Boolean uploadPublicImage(byte[] bytes, String key, String mimeType) throws QiniuException {
        String token = getUploadToken(FileType.PUBLIC_IMAGE);
        Response response = uploadManager.put(bytes, key, token, null, mimeType, true);
        if (response.isOK()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public String getUploadToken(FileType type) {
        return auth.uploadToken(type.getValue());
    }

    public void init() {
        auth = Auth.create(accessKey, secretKey);
        uploadManager = new UploadManager();
    }

    private Auth auth;
    private UploadManager uploadManager;
    private String accessKey;
    private String secretKey;
    private String baseUrl;
    private String img404;
    private String articleDefaultImg;
    private String publicImgBucket;

    public String getAccessKey() {
        return accessKey;
    }

    public QiNiuHelper setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public QiNiuHelper setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public QiNiuHelper setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getImg404() {
        return img404;
    }

    public QiNiuHelper setImg404(String img404) {
        this.img404 = img404;
        return this;
    }

    public String getArticleDefaultImg() {
        return articleDefaultImg;
    }

    public QiNiuHelper setArticleDefaultImg(String articleDefaultImg) {
        this.articleDefaultImg = articleDefaultImg;
        return this;
    }

    public String getPublicImgBucket() {
        return publicImgBucket;
    }

    public QiNiuHelper setPublicImgBucket(String publicImgBucket) {
        this.publicImgBucket = publicImgBucket;
        return this;
    }

    public enum FileType {

        PUBLIC_IMAGE("public-image"),
        PRIVATE_IMAGE("private-image");

        public static FileType getFileType(String value) {
            for (FileType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            return null;
        }

        private String value;

        public String getValue() {
            return value;
        }

        FileType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "FileType{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }
}
