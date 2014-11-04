package datamodel;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2014-11-02.
 */
public class SQLNumberDataTypeTest {

    @Test
    public void testTypeCaching() {
        SQLNumberDataType type1a = SQLNumberDataType.getInstance(10,2);
        SQLNumberDataType type1b = SQLNumberDataType.getInstance(10,2);
        SQLNumberDataType type2 = SQLNumberDataType.getInstance(8,2);
        assertTrue("it must be the same objects, not only equal objects", type1a == type1b);
        assertFalse(type1a == type2);
    }
}
