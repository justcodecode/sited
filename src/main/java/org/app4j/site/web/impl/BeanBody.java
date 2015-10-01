package org.app4j.site.web.impl;

/**
 * @author chi
 */
public class BeanBody implements Body {
    public final Object bean;

    public BeanBody(Object bean) {
        this.bean = bean;
    }
}
