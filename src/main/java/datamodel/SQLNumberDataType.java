package datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-11-01.
 */
public class SQLNumberDataType extends SQLDataType {

    private SQLNumberDataType(int precision, int scale) {
        this.precisionSpecification = new PrecisionSpecification(precision, scale);
    }

    private static class PrecisionSpecification {

        private static final int NOT_SPECIFIED = -1; // we cannot rely on default int value of 0 to mean unspecified value because 0 is allowed for scale

        private final int precision; // total number of digits (both before and after decimal point)
        private final int scale; // number of digits to the right of the decimal point

        private PrecisionSpecification(int precision, int scale) {
            // TODO add max sizes validation
            // TODO add input validation
            this.precision = precision;
            this.scale = scale;
        }

        private PrecisionSpecification(int precision) {
            this.precision = precision;
            this.scale = NOT_SPECIFIED;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PrecisionSpecification that = (PrecisionSpecification) o;

            if (precision != that.precision) return false;
            return scale == that.scale;

        }

        @Override
        public int hashCode() {
            int result = precision;
            result = 31 * result + scale;
            return result;
        }
    }

    private final PrecisionSpecification precisionSpecification;


    private static Map<PrecisionSpecification, SQLNumberDataType> instances = new HashMap<>();

    static SQLNumberDataType getInstance(int precision, int scale) {
        PrecisionSpecification specification = new PrecisionSpecification(precision, scale);
        if (!instances.containsKey(specification)) {
            instances.put(specification, new SQLNumberDataType(precision, scale));
        }
        return instances.get(specification);
    }

    static SQLNumberDataType getInstance(int precision) {
        return getInstance(precision, PrecisionSpecification.NOT_SPECIFIED);
    }

    static SQLNumberDataType getInstance() {
        return getInstance(PrecisionSpecification.NOT_SPECIFIED, PrecisionSpecification.NOT_SPECIFIED);
    }

    @Override
    public int getFieldSizeSpecifier(int index) {
        if (index==0) {
            return precisionSpecification.precision;
        }
        if (index==1) {
            return precisionSpecification.scale;
        }
        throw new IllegalArgumentException("Number data type can have at most two field size specifiers (precision and scale)");
    }

    @Override
    public DataTypeValue valueOfNotNull(String stringValue) {
        // TODO check: we trim to column size, no exception
        // TODO all issues with BigDecimal, maybe save on datatypes, consider length
        // TODO: when for example precision is 2 then short is fone (SQLShortValue), when scale is 0 then int, when there are fields after comma then float or big decimal, and so on.
        if (precisionSpecification.precision == PrecisionSpecification.NOT_SPECIFIED) {
            return new IntegerValue(stringValue);
        }
        return new FloatValue(stringValue);
    }

}
