package ds.top10;


import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CacheConfig {
    public static CacheConfiguration<String, Long> pageviewCache() {
        CacheConfiguration<String, Long> cfg = new CacheConfiguration<>("pageviews");

        // Index the words and their counts,
        // so we can use them for fast SQL querying.
        cfg.setIndexedTypes(String.class, Long.class);

        // Sliding window of 1 second.
        cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                new CreatedExpiryPolicy(new Duration(SECONDS, 1))));

        return cfg;
    }
}