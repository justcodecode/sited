package org.app4j.site.runtime.database;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.runtime.InternalModule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class DatabaseConfig extends InternalModule {
    private final SimpleCodecRegistry simpleCodecRegistry = new SimpleCodecRegistry();
    private MongoDatabase database;

    public SimpleCodecRegistry codecs() {
        return simpleCodecRegistry;
    }

    public MongoDatabase get() {
        return database;
    }

    @Override
    protected void configure() throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI(property("site.database.url").get());
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .codecRegistry(simpleCodecRegistry)
                .build();
        MongoClient mongoClient = new MongoClient(toServerAddresses(mongoClientURI), mongoClientOptions);
        database = mongoClient.getDatabase(mongoClientURI.getDatabase());
        bind(DatabaseConfig.class).to(this).export();
    }

    @Override
    protected String name() {
        return "database";
    }

    private List<ServerAddress> toServerAddresses(MongoClientURI mongoClientURI) {
        List<ServerAddress> serverAddresses = Lists.newArrayList();
        serverAddresses.addAll(mongoClientURI.getHosts().stream().map(ServerAddress::new).collect(Collectors.toList()));
        return serverAddresses;
    }
}
