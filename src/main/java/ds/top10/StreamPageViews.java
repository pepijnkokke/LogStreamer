package ds.top10;


import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.stream.StreamTransformer;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class StreamPageViews {
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start()) {
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.pageviewCache());

            // Create a streamer to stream words into the cache.
            try (IgniteDataStreamer<String, Long> stmr = ignite.dataStreamer(stmCache.getName())) {
                // Allow data updates.
                stmr.allowOverwrite(true);

                // Configure data transformation to count instances of the same word.
                stmr.receiver(StreamTransformer.from(
                        new CacheEntryProcessor<String,Long,Object>() {
                            @Override
                            public Object process(MutableEntry<String, Long> e, Object... arg) throws EntryProcessorException {
                                    // Get current count.
                                    Long oldVal = e.getValue();
                                    Long newVal = (Long) arg[0];

                                    // Increment current count by 1.
                                    e.setValue((oldVal == null ? 0 : oldVal) + newVal);
                                    return null;
                                }
                            }
                ));

                // Stream pageviews from a file.
                InputStream fileStream = new FileInputStream(args[0]);
                InputStream gzipStream = new GZIPInputStream(fileStream);
                InputStreamReader decoder = new InputStreamReader(gzipStream, "UTF-8");
                BufferedReader reader = new BufferedReader(decoder);
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        stmr.addData(parts[1], Long.parseLong(parts[2]));
                    }
                }
                finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        }
    }
}

