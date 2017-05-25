package com.jy.pay.weixin.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jy.pay.common.anno.UrlParam;
import com.jy.pay.common.anno.XmlElement;
import com.jy.pay.common.anno.XmlRootElement;
import com.jy.pay.common.util.DigestUtil;
import com.jy.pay.weixin.helper.config.WeixinPayConfigure;
import com.jy.pay.weixin.helper.request.entity.AccessTokenRequestConfigure;
import com.jy.pay.weixin.helper.request.entity.UnionPayRequestConfigure;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Component
public class WeixinHelper {

    private final Logger logger = LogManager.getLogger(WeixinHelper.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WeixinPayConfigure weixinPayConfigure;

    private static final Map<Class<?>, Map<Field, String>> CONFIGURE_FIELD_URLPARAM_MAPPING = new HashMap<>();
    private static final Map<Class<?>, Map.Entry<RootTag, Map<Field, Entry<String, Boolean>>>> CONFIGURE_FIELD_XML_MAPPING = new HashMap<>();
    private static final List<ValueHandler> xmlElementHandlers = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public Map<String, Object> requestAccessToken(AccessTokenRequestConfigure configure) throws IllegalAccessException, IOException {

        String queryUrl = weixinPayConfigure.getAccessTokenUrl() + buildUrlSearch(configure);
        HttpGet get = new HttpGet(queryUrl);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        return objectMapper.readValue(result, HashMap.class);
    }

    private static ValueHandler getValueHandler(Class clazz) {
        for (ValueHandler valueHandler: xmlElementHandlers) {
            if (valueHandler.support(clazz)) {
                return valueHandler;
            }
        }
        return null;
    }


    public String buildUrlSearch(Object configure) throws IllegalAccessException {
        if (configure == null) {
            throw new NullPointerException("AccessTokenRequestConfigure is null");
        }
        Map<Field, String> mapping = CONFIGURE_FIELD_URLPARAM_MAPPING.get(configure.getClass());
        if (mapping == null) {
            return "";
        }
        StringBuilder urlBuilder = new StringBuilder();
        for (Map.Entry<Field, String> entry: mapping.entrySet()) {
            Field f = entry.getKey();
            if (urlBuilder.length() == 0) {
                urlBuilder.append('?');
            }
            else {
                urlBuilder.append('&');
            }
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            String urlParam = entry.getValue();
            String paramValue = (String)f.get(configure);
            urlBuilder.append(urlParam).append("=").append(paramValue);
        }
        return urlBuilder.toString();
    }

    @SuppressWarnings("unchecked")
    public String buildXml (Object configure, boolean isTop) throws IllegalAccessException {
        if (configure == null || configure.getClass() == null || CONFIGURE_FIELD_XML_MAPPING.get(configure.getClass()) == null) {
            return "";
        }
        Map.Entry<RootTag, Map<Field, Entry<String, Boolean>>> xmlFieldMapping = CONFIGURE_FIELD_XML_MAPPING.get(configure.getClass());
        RootTag rootTag = xmlFieldMapping.getKey();

        StringBuilder sb = new StringBuilder();
        if (isTop) {
            sb.append(rootTag.getNamespace());
            appendTagBegin(sb, rootTag.getTagName());
        }
        Map<Field, Entry<String, Boolean>> fieldMapping = xmlFieldMapping.getValue();
        for (Map.Entry<Field, Entry<String, Boolean>> entry: fieldMapping.entrySet()) {
            Field f = entry.getKey();
            Entry<String, Boolean> tagEntry = entry.getValue();
            //tag head
            appendTagBegin(sb, tagEntry.getKey());
            ValueHandler valueHandler = getValueHandler(f.getType());
            //complex type
            if (valueHandler == null) {
                Object tagValue = f.get(configure);
                String innerFieldXml = buildXml(tagValue, false);
                sb.append(innerFieldXml);
            }
            //simple type
            else {
                Object tagValue = f.get(configure);
                //need to escape
                if (tagEntry.getValue()) {
                    escapeTagValue(sb, (String)tagValue);
                }
                else {
                    sb.append(valueHandler.handle(tagValue));
                }
            }
            //tag tail
            appendTagEnd(sb, tagEntry.getKey());
        }
        if (isTop) {
            appendTagEnd(sb, rootTag.getTagName());
        }
        return sb.toString();
    }

    public String getSign(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                String tagName;
                String value = f.get(o).toString();
                XmlElement e = f.getAnnotation(XmlElement.class);
                if (e!= null) {
                    tagName = e.value();
                    if (e.escape()) {
                        if (value.contains("<![CDATA[")) {
                            value = value.substring(9, value.length() - 3);
                        }
                    }
                }
                else {
                    tagName = f.getName();
                }
                list.add(tagName + "=" + value + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + weixinPayConfigure.getKey();
        logger.info("Sign Before SHA1:" + result);
        byte[] digestBytes = DigestUtil.getSha1().digest(result.getBytes());
        result = DatatypeConverter.printHexBinary(digestBytes).toUpperCase();
        logger.info("Sign Result:" + result);
        return result;
    }


    public String getSign(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + weixinPayConfigure.getKey();
        logger.info("Sign Before SHA1:" + result);
        byte[] digestBytes = DigestUtil.getSha1().digest(result.getBytes());
        result = DatatypeConverter.printHexBinary(digestBytes).toUpperCase();
        logger.info("Sign Result:" + result);
        return result;
    }

    private WeixinHelper appendTagBegin(StringBuilder sb, String tagName) {
        sb.append('<').append(tagName).append('>');
        return this;
    }
    private WeixinHelper appendTagEnd(StringBuilder sb, String tagName) {
        sb.append("</").append(tagName).append('>');
        return this;
    }
    private WeixinHelper escapeTagValue(StringBuilder sb, String tagValue) {
        sb.append("<![CDATA[").append(tagValue).append("]]>");
        return this;
    }

    static {
        xmlElementHandlers.add(new StringValueHandler());
    }

    //load UrlParam class
    static {
        Map<Field, String> accessTokenRequestConfigureClassMap = new HashMap<>();
        Class<AccessTokenRequestConfigure> accessTokenRequestConfigureClass =  AccessTokenRequestConfigure.class;
        Field[] accessTokenRequestConfigureClassFields = accessTokenRequestConfigureClass.getDeclaredFields();
        for (Field f: accessTokenRequestConfigureClassFields) {
            String urlParamName;
            UrlParam urlParam = f.getAnnotation(UrlParam.class);
            if (urlParam != null) {
                urlParamName = urlParam.value();
                if (urlParamName == null || urlParamName.trim().length() == 0) {
                    urlParamName = f.getName();
                }
            }
            else {
                urlParamName = f.getName();
            }
            accessTokenRequestConfigureClassMap.put(f, urlParamName);
        }
        CONFIGURE_FIELD_URLPARAM_MAPPING.put(accessTokenRequestConfigureClass, accessTokenRequestConfigureClassMap);
    }
    //load xml config class
    static {
        analyseXmlConfigMapping(UnionPayRequestConfigure.class);
    }

    /**
     * 说明
     * 功能: 解析XML实体类
     * 参与注解: {@link XmlRootElement}, {@link XmlElement}
     * 解析结果缓存结构:<Class<?>, Map.Entry<RootTag, Map<Field, Object>>>
     *     key: bean 类名
     *     value: Entry
     *     -------------------------------------------------------------
     *     value.Entry.key: 跟标签,包含标签名称和文档声明
     *     value.Entry.value: 类属性与标签名称的映射
     *          结构: Field <----> Entry<String, Boolean>, Entry.key表示标签名称, Entry.value 表示是否需要转义
     *          1.当属性(Field)为简单类型时,标签名称取自{@link XmlElement#value()}
     *          2.当属性(Field)为复杂类型时,去标签名称的优先级为: {@link XmlElement#value()} > {@link XmlRootElement#value()}
     *
     * 功能总结: 采集类的属性与标签的映射关系,以及标签是否余姚被转义
     * */
    private static void analyseXmlConfigMapping(Class<?> clazz) {
        if (clazz == null || CONFIGURE_FIELD_XML_MAPPING.containsKey(clazz)) {
            return;
        }
        XmlRootElement xmlRootElement = clazz.getAnnotation(XmlRootElement.class);
        String rootTag;
        String declaration = xmlRootElement.declaration();
        if (xmlRootElement == null) {
            rootTag = clazz.getSimpleName();
        }
        else {
            rootTag = xmlRootElement.value();
        }
        Entry<RootTag, Map<Field, Entry<String, Boolean>>> xmlConfigureMapping = new Entry<>(new RootTag(rootTag, declaration));
        CONFIGURE_FIELD_XML_MAPPING.put(clazz, xmlConfigureMapping);
        Map<Field, Entry<String, Boolean>> xmlFiledsConfigureMapping = new HashMap();
        xmlConfigureMapping.setValue(xmlFiledsConfigureMapping);
        Field[] unionPayRequestConfigureClassFields = clazz.getDeclaredFields();
        for (Field f: unionPayRequestConfigureClassFields) {
            //非静态变量
            if (!Modifier.isStatic(f.getModifiers())) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Class<?> fieldType = f.getType();
                XmlRootElement innerXmlRootElement = fieldType.getAnnotation(XmlRootElement.class);
                XmlElement e = f.getAnnotation(XmlElement.class);
                String tagName;
                boolean escape;
                if (innerXmlRootElement != null) {
                    if (!CONFIGURE_FIELD_XML_MAPPING.containsKey(fieldType)) {
                        analyseXmlConfigMapping(fieldType);
                    }
                    if (e != null) {
                        tagName = e.value();
                    }
                    else if (innerXmlRootElement.value() != null && innerXmlRootElement.value().trim().length() > 0) {
                        tagName = innerXmlRootElement.value();
                    }
                    else {
                        tagName = f.getName();
                    }
                    escape = false;
                }
                else {
                    if (e != null) {
                        tagName = e.value();
                    }
                    else {
                        tagName = f.getName();
                    }
                    escape = e.escape();
                }
                xmlFiledsConfigureMapping.put(f, new Entry<String, Boolean>(tagName, escape));
            }
        }
    }

    public interface ValueHandler {
        boolean support(Class<?> clazz);
        String handle(Object t);
    }
    public static abstract class AbstractValueHandler implements ValueHandler {

        @Override
        public boolean support(Class<?> clazz) {
            return t == clazz;
        }

        private final Class<?> t;
        public AbstractValueHandler(Class<?> t) {
            this.t = t;
        }
    }
    public static class StringValueHandler extends AbstractValueHandler {
        @Override
        public String handle(Object s) {
            return (String) s;
        }

        public StringValueHandler() {
            super(String.class);
        }
    }

    private static class RootTag {
        private String tagName;
        private String namespace;

        public String getTagName() {
            return tagName;
        }

        public RootTag setTagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public String getNamespace() {
            return namespace;
        }

        public RootTag setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public RootTag(String tagName, String namespace) {
            this.tagName = tagName;
            this.namespace = namespace;
        }
    }

    private static class Entry<K, V> implements Map.Entry<K, V> {

        private K k;

        private V v;

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }

        @Override
        public V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }

        public Entry(K k) {
            this.k = k;
        }

        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

}

