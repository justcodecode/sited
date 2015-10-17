package org.app4j.site.runtime.track.service;

import org.app4j.site.runtime.track.domain.Tracking;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;

import java.util.Date;
import java.util.Map;

public class TrackingCodec implements Codec<Tracking> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Tracking decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Tracking tracking = new Tracking();
        tracking.setId(document.get("id", String.class));
        tracking.setActor(document.get("actor", String.class));
        tracking.setAction(document.get("action", String.class));
        tracking.setTarget(document.get("target", String.class));
        tracking.setContext(document.get("context", Map.class));
        tracking.setCreateTime(document.get("createTime", Date.class));
        tracking.setStatus(document.get("status", int.class));
        return tracking;
    }

    @Override
    public void encode(BsonWriter writer, Tracking tracking, EncoderContext encoderContext) {
        Document document = new Document();
        document.put("id", tracking.getId());
        document.put("actor", tracking.getActor());
        document.put("action", tracking.getAction());
        document.put("target", tracking.getTarget());
        document.put("context", tracking.getContext());
        document.put("createTime", tracking.getCreateTime());
        document.put("status", tracking.getStatus());
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Tracking> getEncoderClass() {
        return Tracking.class;
    }
}
