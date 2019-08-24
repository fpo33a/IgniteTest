package IgniteTest;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;

import javax.cache.Cache;

public class AppRunnable implements IgniteRunnable {

    @IgniteInstanceResource
    Ignite ignite;

    final long cityId = 2; // Id for Denver

    @Override
    public void run() {
        // Getting an access to Persons cache.
        IgniteCache<BinaryObject, BinaryObject> people = ignite.cache("Person").withKeepBinary();

        ScanQuery<BinaryObject, BinaryObject> query = new ScanQuery <BinaryObject, BinaryObject>();

        try (QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> cursor = people.query(query)) {

            // Iteration over the local cluster node data using the scan query.
            for (Cache.Entry<BinaryObject, BinaryObject> entry : cursor) {
                BinaryObject personKey = entry.getKey();

                // Picking Denver residents only only.
                if (personKey.<Long>field("CITY_ID") == cityId) {
                    BinaryObject person = entry.getValue();

                    // Sending the warning message to the person.
                    System.out.println("person = "+person);
                }
            }
        }
    }
}
