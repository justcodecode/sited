package org.app4j.site.internal.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Map;

/**
 * @author chi
 */
public class SimpleCodecRegistry implements CodecRegistry {
    private final DocumentCodec documentCodec = new DocumentCodec();
    private final Map<Class<?>, Codec<?>> codecs = Maps.newHashMap();
    private final Map<Class<?>, DomainCodec<?>> domainCodecs = Maps.newHashMap();

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

    @SuppressWarnings("unchecked")
    public <T> DomainCodec<T> domainCodec(Class<T> type) {
        Preconditions.checkState(domainCodecs.containsKey(type), "missing codec for %s", type.getName());
        return (DomainCodec<T>) domainCodecs.get(type);
    }

    public <T> SimpleCodecRegistry add(DomainCodec<T> domainCodec) {
        domainCodecs.put(domainCodec.domainType(), domainCodec);
        codecs.put(domainCodec.domainType(), new Codec<T>() {
            @Override
            public T decode(BsonReader bsonReader, DecoderContext decoderContext) {
                Document document = documentCodec.decode(bsonReader, decoderContext);
                return domainCodec.from(document);
            }

            @Override
            public void encode(BsonWriter bsonWriter, T t, EncoderContext encoderContext) {
                Document document = domainCodec.to(t);
                documentCodec.encode(bsonWriter, document, encoderContext);
            }

            @Override
            public Class<T> getEncoderClass() {
                return domainCodec.domainType();
            }
        });
        return this;
    }
}
