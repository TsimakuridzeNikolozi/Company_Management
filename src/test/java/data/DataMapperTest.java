package data;

import data.entity.Person;
import org.junit.jupiter.api.Test;
import static junit.framework.TestCase.*;

public class DataMapperTest {

    @Test
    public void testGetMapping() {
        String expected = "person";
        String actual = DataMapper.getMapping(Person.class);
        assertEquals(expected, actual);
    }
}