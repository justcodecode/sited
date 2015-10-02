import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.mongodb.MongoClientURI;
import org.app4j.site.Site;
import org.app4j.site.Sited;

import java.io.File;
import java.util.Map;

/**
 * @author chi
 */
public class Main {
    protected static Map<String, String> profile(String[] args) {
        Map<String, String> arguments = Maps.newHashMap();
        for (int i = 1, length = args.length; i < length; i++) {
            String arg = args[i];
            Preconditions.checkState(arg.startsWith("--") && arg.contains("="), "arg must be in --{name}={value} format, arg={}", arg);
            int index = arg.indexOf('=');
            arguments.put(arg.substring(2, index), arg.substring(index + 1));
        }
        return arguments;
    }


    public static void main(String[] args) {
        Map<String, String> profile = profile(args);
        if (profile.containsKey("dir") && profile.containsKey("db")) {
            File dir = new File(profile.get("dir"));
            MongoClientURI mongoClientURI = new MongoClientURI(profile.get("db"));
            new Sited(new Site(dir, mongoClientURI)).start();
        }
    }
}
