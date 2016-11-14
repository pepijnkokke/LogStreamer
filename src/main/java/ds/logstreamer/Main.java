package ds.logstreamer;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        OptionParser parser = new OptionParser()
        {{
            accepts("serve", "Start Ignite server");
            accepts("stream-from", "Start data streamer from [file]").withRequiredArg().ofType(File.class).describedAs("file");
            accepts("query-every", "Query Ignite cache every [millis]").withRequiredArg().ofType(Long.class).describedAs("millis");
            accepts("config", "Set Ignite config file to [config]").withRequiredArg().ofType(File.class).describedAs("config");
        }};

        OptionSet options = parser.parse(args);
        String config = null;
        if (options.has("config")) {
            config = ((File) options.valueOf("config")).getAbsolutePath();
        }
        if (options.has("serve")) {
            if (config == null) {
                Ignition.start();
            }
            else {
                Ignition.start(config);
            }
        }
        else if (options.has("stream-from")) {
            StreamPageViews.from(config, (File) options.valueOf("stream-from"));
        }
        else if (options.has("query-every")) {
            QueryPageViews.every(config, (Long) options.valueOf("query-every"));
        }
        else {
            System.out.println("Usage: logstreamer [options]");
            System.out.println();
            parser.printHelpOn(System.out);
            System.out.println();
        }
    }
}
