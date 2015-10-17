import org.app4j.site.Site;
import org.app4j.site.Sited;
import org.jboss.logging.Logger;

import java.io.File;

/**
 * @author chi
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            File dir = new File(args[0]);
            new Sited(new Site(dir)).start();
        } else {
            Logger.getLogger(Main.class).error(Sited.usage());
        }
    }
}
