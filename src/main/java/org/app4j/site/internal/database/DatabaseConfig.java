package org.app4j.site.internal.database;

import com.mongodb.client.MongoDatabase;

/**
 * @author chi
 */
public interface DatabaseConfig {
    SimpleCodecRegistry codecs();

    MongoDatabase get();
}
