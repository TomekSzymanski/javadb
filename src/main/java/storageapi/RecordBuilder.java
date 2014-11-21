package storageapi;

import datamodel.BooleanValue;
import datamodel.IntegerValue;
import datamodel.VarcharValue;

/**
 * Created on 2014-11-18.
 */
public class RecordBuilder { // TODO:Q: it is used in test code only, should it be in production code (in the same package as Record)?

    private final Record values = new Record();

    public RecordBuilder varchar(String string) {
        values.add(new VarcharValue(string));
        return this;
    }

    public RecordBuilder number(int number) {
        values.add(new IntegerValue(number));
        return this;
    }

    public RecordBuilder bool(boolean value) {
        values.add(BooleanValue.valueOf(value));
        return this;
    }
    public Record build() {
        return values;
    }

    public static RecordBuilder newRecord() {
        return new RecordBuilder();
    }

}
