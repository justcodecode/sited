package org.app4j.site.runtime.index;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.app4j.site.Module;
import org.app4j.site.runtime.database.SimpleCodecRegistry;

import java.io.File;
import java.util.Map;

/**
 * @author chi
 */
public class IndexModule extends Module implements IndexConfig {
    private final Map<String, Index<?>> indices = Maps.newHashMap();
    private File indexDir;
    private SimpleCodecRegistry codecRegistry;

    @Override
    @SuppressWarnings("unchecked")
    public <T> Index<T> index(String name) {
        Preconditions.checkState(indices.containsKey(name), "missing index %s", name);
        return (Index<T>) indices.get(name);
    }

    @Override
    public <T> Index<T> createIndex(String name, Class<T> type, IndexLoader<T> indexLoader) {
        Index<T> index = new Index<>(new File(indexDir, name), indexLoader, new StandardAnalyzer(), codecRegistry.domainCodec(type));
        indices.put(name, index);
        return index;
    }

    @Override
    protected void configure() throws Exception {
        indexDir = site().dir("index");
        codecRegistry = database().codecs();
    }
}
