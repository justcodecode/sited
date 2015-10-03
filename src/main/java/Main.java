import com.mongodb.MongoClientURI;
import org.app4j.site.Site;
import org.app4j.site.Sited;
import org.jboss.logging.Logger;

/**
 * @author chi
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            MongoClientURI mongoClientURI = new MongoClientURI(args[0]);
            new Sited(new Site(mongoClientURI)).start();
        } else {
            Logger.getLogger(Main.class).error(Sited.usage());
        }
    }
}
