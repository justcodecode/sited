package org.app4j.site.runtime.i18n;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.app4j.site.Module;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.template.TemplateConfig;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author chi
 */
public class I18nConfig extends InternalModule {
    public final Map<String, List<I18nSupplier>> messages = Maps.newHashMap();

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(TemplateConfig.class);
    }

    @Override
    protected void configure() throws Exception {
//        site().template().dialect()
//                .add(StandardTextAttrProcessor.class, new I18nTextProcessor(this))
//                .add(new I18nTextProcessor(this));

        bind(I18nConfig.class).to(this).export();
    }

    public synchronized I18nConfig add(String lang, String path) {
        URL resource = com.google.common.io.Resources.getResource(path);
        try (InputStreamReader reader = new InputStreamReader(resource.openStream(), Charsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);
            return add(lang, new PropertyI18nSupplier(properties));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized I18nConfig add(String lang, I18nSupplier i18nSupplier) {
        if (messages.containsKey(lang)) {
            messages.get(lang).add(i18nSupplier);
        } else {
            messages.put(lang, Lists.newArrayList(i18nSupplier));
        }
        return this;
    }


    public String message(String key, String lang, Object... args) {
        if (messages.containsKey(lang)) {
            String message = null;

            for (I18nSupplier i18nSupplier : messages.get(lang)) {
                message = i18nSupplier.get(key, lang, args);
                if (message != null) {
                    break;
                }
            }

            return message == null ? key : message;
        } else {
            return key;
        }
    }

    class PropertyI18nSupplier implements I18nSupplier {
        final Properties properties;

        PropertyI18nSupplier(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String get(String key, Object... args) {
            return key != null && properties.containsKey(key)
                    ? MessageFormatter.arrayFormat(properties.getProperty(key), args).getMessage() : null;
        }
    }
}
