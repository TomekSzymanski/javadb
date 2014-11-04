package datamodel;

import clientapi.SQLException;

import java.util.List;

/**
 * Created on 2014-11-02.
 */
public class SQLDataTypeFactory {

    public static SQLDataType getInstance(String dataTypeString, List<Integer> fieldSizeSpecifiers) {
        int numberOfFieldSizeSpecifiers = fieldSizeSpecifiers.size();
        switch (dataTypeString) {
            case SQLDataType.NUMBER:
                if (numberOfFieldSizeSpecifiers == 0) {
                    return SQLNumberDataType.getInstance();
                }
                if (numberOfFieldSizeSpecifiers == 1) {
                    return SQLNumberDataType.getInstance(fieldSizeSpecifiers.get(0));
                }
                if (numberOfFieldSizeSpecifiers == 2) {
                    return SQLNumberDataType.getInstance(fieldSizeSpecifiers.get(0), fieldSizeSpecifiers.get(1));
                }
                throw new IllegalArgumentException("When specifying precision for NUMBER data type you can specify either precision, or precision and scale");
            case SQLDataType.BOOLEAN:
                if (numberOfFieldSizeSpecifiers != 0) {
                    throw new IllegalArgumentException("cannot specify field size for BOOLEAN data type");
                }
                return SQLBooleanDataType.getInstance();
            case SQLDataType.VARCHAR:
                if (numberOfFieldSizeSpecifiers != 1) {
                    throw new IllegalArgumentException("exactly one field size specifier must be specified for VARCHAR");
                }
                return SQLVarcharDataType.getInstance(fieldSizeSpecifiers.get(0));
            default:
                throw new SQLException("Unrecognized column data type " + dataTypeString);
        }
    }
}
