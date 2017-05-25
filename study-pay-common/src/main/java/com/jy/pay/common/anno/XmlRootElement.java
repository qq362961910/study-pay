package com.jy.pay.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XmlRootElement {
    /**
     * declaration of the XML.
     */
    String declaration() default "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    /**
     * local name of the XML element.
     * <p>
     * If the value is "##default", then the name is derived from the
     * class name.
     *
     */
    String value();
}
