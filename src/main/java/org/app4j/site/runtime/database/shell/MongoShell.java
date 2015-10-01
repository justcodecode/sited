package org.app4j.site.runtime.database.shell;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.client.MongoDatabase;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * @author chi
 */
public class MongoShell {
    private final MongoDatabase db;
    private final Cache<String, CompiledScript> cache = CacheBuilder.newBuilder().build();
    private final ScriptEngine scriptEngine;

    public MongoShell(MongoDatabase db) {
        this.db = db;
        scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public Object eval(String expression, Bindings bindings) throws ScriptException {
        CompiledScript script = cache.getIfPresent(expression);
        Bindings simpleBindings = new SimpleBindings();

        if (bindings != null) {
            simpleBindings.putAll(bindings);
        }
        simpleBindings.put("db", new ShellDB(db));
        if (script == null) {
            script = ((Compilable) scriptEngine).compile(expression);
            cache.put(expression, script);
        }
        return script.eval(simpleBindings);
    }
}
