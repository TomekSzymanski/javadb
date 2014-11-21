package datamodel;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * Created on 2014-11-11.
 */
public class SQLDataTypeFactoryTest {

    @Test
    public void createNumberDataTypeWithPrecisionAndScale() {
        SQLDataType type = SQLDataTypeFactory.getInstance("NUMBER", Arrays.asList(8,2));
        assertEquals(SQLNumberDataType.class, type.getClass());
        assertEquals(8, type.getFieldSizeSpecifier(0));
        assertEquals(2, type.getFieldSizeSpecifier(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void factoryCalledToCreateUnknownSQLDataType() {
        SQLDataTypeFactory.getInstance("BIGINTEGERWHATEVER", Collections.EMPTY_LIST);
    }
}
