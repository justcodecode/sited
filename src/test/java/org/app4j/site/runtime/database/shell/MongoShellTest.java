package org.app4j.site.runtime.database.shell;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * @author chi
 */
public class MongoShellTest {
    @Rule
    public FongoRule fongoRule = new FongoRule();

    MongoShell mongoShell;

    @Before
    public void setup() {
        MongoDatabase db = fongoRule.getDatabase();
        mongoShell = new MongoShell(db);
        db.getCollection("Test").insertOne(new Document("field", new Document("name", "some")));
    }


    @Test
    public void eval() throws ScriptException {
        ShellCursor value = (ShellCursor) mongoShell.eval("db.Test.find({field: { name: 'some1'}})", new SimpleBindings());
        System.out.println(value.total());
    }
}