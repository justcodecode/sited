package org.app4j.site.runtime.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Map;

/**
 * @author chi
 */
public class SimpleCodecRegistry implements CodecRegistry {
    private final Map<Class<?>, Codec<?>> codecs = Maps.newHashMap();

    public SimpleCodecRegistry() {
        codecs.put(Document.class, new DocumentCodec());
    }

    public <T> SimpleCodecRegistry add(Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz) {
        Preconditions.checkState(codecs.containsKey(clazz), "missing codec for %s", clazz.getName());
        return (Codec<T>) codecs.get(clazz);
    }
}
