package datamodel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created on 2014-11-11.
 */
public class IntegerValueTest {
    @Test
    public void testInit() {
        assertEquals(4, (new IntegerValue(4)).intValue());
    }
}