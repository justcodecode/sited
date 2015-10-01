package org.app4j.site.runtime.database.shell;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public interface ScriptObjectMirrorHelper {
    @SuppressWarnings("unchecked")
    default <T> T toDocument(ScriptObjectMirror scriptObject) {
        switch (scriptObject.getClassName()) {
            case "Object":
                Document document = new Document();
                for (Map.Entry<String, Object> entry : scriptObject.entrySet()) {
                    if (entry.getValue() instanceof ScriptObjectMirror) {
                        document.put(entry.getKey(), toDocument((ScriptObjectMirror) entry.getValue()));
                    } else {
                        document.put(entry.getKey(), entry.getValue());
                    }
                }
                return (T) document;
            case "Array":
                List list = scriptObject.to(List.class);

                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if (item instanceof ScriptObjectMirror) {
                        list.set(i, toDocument((ScriptObjectMirror) item));
                    }
                }

                return (T) list;
            default:
                throw new RuntimeException("not support type yet" + scriptObject.getClassName());

        }
    }
}
