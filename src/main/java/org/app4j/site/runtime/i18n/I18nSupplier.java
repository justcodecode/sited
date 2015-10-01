package org.app4j.site.runtime.i18n;

/**
 * @author chi
 */
public interface I18nSupplier {
    String get(String key, Object... args);
}
