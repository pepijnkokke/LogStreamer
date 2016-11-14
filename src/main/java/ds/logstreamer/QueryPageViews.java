package ds.logstreamer;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.io.IOException;
import java.util.List;


public class QueryPageViews {
    public static void every(String config, long millis) throws IOException, InterruptedException {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = (config == null) ? Ignition.start() : Ignition.start(config)) {
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.pageviewCache());

            // Select top 10 words.
            SqlFieldsQuery top10Qry = new SqlFieldsQuery(
                    "select _key, _val from Long order by _val desc limit 10");

            // Query top 10 popular words every 10 seconds.
            while (true) {
                // Execute queries.
                List<List<?>> resp = stmCache.query(top10Qry).getAll();

                // Print top 10 words.
                Long timestamp = System.currentTimeMillis() / 1000L;

                for (int i = 0; i <= 10; i++) {
                    if (i < resp.size()) {
                        List<?> result = resp.get(i);

                        Object page = result.get(0);
                        Object count = result.get(1);
                        System.out.println(timestamp+":"+count+":"+page);
                    }
                }

                Thread.sleep(millis);
            }
        }
    }
}