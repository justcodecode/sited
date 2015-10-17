package org.app4j.site.runtime.index;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.runtime.database.Dumper;
import org.app4j.site.runtime.database.SimpleCodecRegistry;
import org.app4j.site.runtime.event.Task;
import org.app4j.site.runtime.index.service.Index;

import java.io.File;
import java.util.Map;

/**
 * @author chi
 */
public class IndexModule extends Module implements IndexConfig {
    private final Map<String, Index<?>> indices = Maps.newHashMap();
    private File indexDir;
    private SimpleCodecRegistry codecRegistry;

    public IndexModule(Site site) {
        super(site);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Index<T> index(String name) {
        Preconditions.checkState(indices.containsKey(name), "missing index %s", name);
        return (Index<T>) indices.get(name);
    }

    @Override
    public <T> Index<T> createIndex(String name, Class<T> type, Dumper<T> dumper) {
        Index<T> index = new Index<>(new File(indexDir, name), dumper, new StandardAnalyzer(), codecRegistry.domainCodec(type));
        indices.put(name, index);

        if (index.isEmpty()) {
            event().scheduler().execute(new Task(name) {
                @Override
                public void run() {
                    index.rebuild();
                }
            });
        }
        return index;
    }

    @Override
    protected void configure() throws Exception {
        bind(IndexConfig.class).to(this).export();

        indexDir = site().dir("index");
        codecRegistry = database().codecs();
        onShutdown(() -> indices.values().stream().forEach(Index::stop));
    }
}
