package IgniteTest;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import java.util.List;

/**
 C:\frank\apache-ignite-2.7.0-bin\apache-ignite-2.7.0-bin\bin>sqlline.bat --color=false --verbose=true -u jdbc:ignite:thin://127.0.0.1/

 DROP TABLE Person;

 CREATE TABLE Person (
 id LONG, name VARCHAR, city_id LONG, PRIMARY KEY (id, city_id))
 WITH "backups=1, cache_name=App_Person";


 CREATE INDEX idx_person_name ON Person (name);
 INSERT INTO Person (id, name, city_id) VALUES (1, 'John Doe', 3);
 INSERT INTO Person (id, name, city_id) VALUES (2, 'Jane Roe', 2);
 INSERT INTO Person (id, name, city_id) VALUES (3, 'Mary Major', 1);
 INSERT INTO Person (id, name, city_id) VALUES (4, 'Richard Miles', 2);

 Result after execution:

 0: jdbc:ignite:thin://127.0.0.1/> select * from Person;
 +--------------------------------+--------------------------------+--------------------------------+
 |               ID               |              NAME              |            CITY_ID             |
 +--------------------------------+--------------------------------+--------------------------------+
 | 3                              | Mary Major                     | 1                              |
 | 2                              | Jane Roe                       | 2                              |
 | 1                              | John                           | 40                             |
 | 2                              | Jane                           | 50                             |
 | 4                              | Richard                        | 70                             |
 | 4                              | Richard Miles                  | 2                              |
 | 3                              | Mary                           | 60                             |
 | 1                              | John Doe                       | 3                              |
 +--------------------------------+--------------------------------+--------------------------------+
 8 rows selected (0,063 seconds)
 0: jdbc:ignite:thin://127.0.0.1/> !quit
 Closing: org.apache.ignite.internal.jdbc.thin.JdbcThinConnection
 C:\frank\apache-ignite-2.7.0-bin\apache-ignite-2.7.0-bin\bin>

 */
public class App 
{
    private static final String PERSON_CACHE = "App_Person";

    public static void main( String[] args )
    {
        System.out.println( "Hello Ignite!" );
        App app = new App ();
        app.TestIgnite();
    }

    public void TestIgnite ()
    {
        Ignite ignite = Ignition.start();

        CacheConfiguration<Long, Person> personCacheCfg = new CacheConfiguration<>(PERSON_CACHE);
        personCacheCfg.setIndexedTypes(Long.class, Person.class);

         try {
            IgniteCache<Long, Person> personCache = ignite.getOrCreateCache(personCacheCfg) ;
            insert(personCache);
            select(personCache);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Cache query DML example finished.");
    }

    private void insert(IgniteCache<Long, Person> personCache) {
        // Insert persons.
        SqlFieldsQuery qry = new SqlFieldsQuery("insert into Person (id, name, city_id) values (?, ?, ?)");

        personCache.query(qry.setArgs(1L,  "John",40));
        personCache.query(qry.setArgs(2L,  "Jane", 50));
        personCache.query(qry.setArgs(3L,  "Mary", 60));
        personCache.query(qry.setArgs(4L,  "Richard", 70));
    }
    /**
     * Query current data.
     *
     * @param personCache Person cache.
     */
    private void select(IgniteCache<Long, Person> personCache) {
        String sql = "select id, name, city_id from Person ";

        List<List<?>> res = personCache.query(new SqlFieldsQuery(sql).setDistributedJoins(true)).getAll();
        for (Object next : res) System.out.println(">>>     " + next);
    }
}
