package IgniteTest;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Person class.
 */
public class Person implements Serializable {
    /** */
    private static final AtomicLong ID_GEN = new AtomicLong();

    /** Person ID . */
    @QuerySqlField
    public Long id;

    /** name (indexed). */
    @QuerySqlField(index = true)
    public String name;


    /** City Id */
    @QuerySqlField
    public Long city_id;

    /**
     * Default constructor.
     */
    public Person() {
        // No-op.
    }

    /**
     * Constructs person record.
     *
     * @param p_id Id.
     * @param p_name  name.
     * @param p_city_id  city id.
     */
    public Person(Long p_id, String p_name, Long p_city_id) {
        id = p_id;

        this.name = p_name;
        this.city_id = p_city_id;
    }



    /**
     * {@inheritDoc}
     */
    @Override public String toString() {
        return "Person [id=" + id +
                ", name=" + name +
                ", city_id=" + city_id + ']';
    }
}
