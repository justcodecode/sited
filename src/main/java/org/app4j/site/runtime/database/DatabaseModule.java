package org.app4j.site.runtime.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.Site;
import org.app4j.site.runtime.InternalModule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class DatabaseModule extends InternalModule implements DatabaseConfig {
    private final SimpleCodecRegistry simpleCodecRegistry = new SimpleCodecRegistry();
    private MongoDatabase database;

    public DatabaseModule(Site site) {
        super(site);
    }

    public SimpleCodecRegistry codecs() {
        return simpleCodecRegistry;
    }

    public MongoDatabase get() {
        return database;
    }

    @Override
    protected void configure() throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI(property("site.db").get());
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
            .codecRegistry(simpleCodecRegistry)
            .build();
        MongoClient mongoClient = new MongoClient(serverAddresses(mongoClientURI), mongoClientOptions);
        database = mongoClient.getDatabase(mongoClientURI.getDatabase());

        bind(DatabaseConfig.class).to(this).export();
    }

    private List<ServerAddress> serverAddresses(MongoClientURI mongoClientURI) {
        return mongoClientURI.getHosts().stream().map(ServerAddress::new).collect(Collectors.toList());
    }
}
