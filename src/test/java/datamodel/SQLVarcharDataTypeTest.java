package datamodel;

import clientapi.SQLException;
import org.junit.Test;

/**
 * Created on 2014-11-11.
 */
public class SQLVarcharDataTypeTest {

    @Test (expected = SQLException.class)
    public void valueOfNotNull() {
        SQLVarcharDataType varchar10DataType = SQLVarcharDataType.getInstance(10);
        varchar10DataType.valueOfNotNull("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }
}
