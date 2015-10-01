package org.app4j.site.runtime.database;

import com.google.common.collect.Lists;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author chi
 */
public abstract class DefaultCodec<T> implements Codec<T> {
    protected List<String> readStringList(String name, BsonReader reader) {
        List<String> list = Lists.newArrayList();

        reader.readName(name);
        if (reader.getCurrentBsonType() != BsonType.NULL) {
            reader.readStartArray();

            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                list.add(reader.readString());
            }

            reader.readEndArray();
        } else {
            reader.readNull();
        }

        return list;
    }

    protected String readString(String name, BsonReader reader) {
        reader.readName(name);
        if (reader.getCurrentBsonType() == BsonType.NULL) {
            reader.readNull();
            return null;
        } else {
            return reader.readString();
        }
    }

    protected void writeString(String name, String value, BsonWriter writer) {
        if (value == null) {
            writer.writeNull(name);
        } else {
            writer.writeString(name, value);
        }
    }

    protected void writeStringList(String name, List<String> values, BsonWriter writer) {
        if (values == null) {
            writer.writeNull(name);
        } else {
            writer.writeName(name);
            writer.writeStartArray();
            for (String value : values) {
                writer.writeString(value);
            }
            writer.writeEndArray();
        }
    }

    protected void writeObjectId(String value, BsonWriter bsonWriter) {
        if (value == null) {
            bsonWriter.writeObjectId("_id", new ObjectId());
        } else {
            bsonWriter.writeObjectId("_id", new ObjectId(value));
        }
    }
}
