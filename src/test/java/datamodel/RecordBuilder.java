package datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014-11-18.
 */
public class RecordBuilder { // TODO: does it fit here?

    private final List<DataTypeValue> values = new ArrayList<>();

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
    public List<DataTypeValue> build() {
        return values;
    }

    public static RecordBuilder buildRecord() {
        return new RecordBuilder();
    }

    public static List<DataTypeValue> emptyRecord() {
        List<DataTypeValue> record = new ArrayList<>();
        return record;
    }

    public static List<List<DataTypeValue>> emptyRecordList() {
        List<List<DataTypeValue>> records = new ArrayList<>();
        return records;
    }

    public static List<DataTypeValue> createRecord(String... stringValues) { // TODO move it from here, it does not fit here
        List<DataTypeValue> record = new ArrayList<>();
        for (String value : stringValues) {
            record.add(new VarcharValue(value));
        }
        return record;
    }
}
