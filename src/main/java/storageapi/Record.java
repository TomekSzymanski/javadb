package storageapi;

import datamodel.DataTypeValue;
import datamodel.VarcharValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2014-11-20.
 */
public class Record implements Iterable<DataTypeValue> {
    private List<DataTypeValue> values = new ArrayList<>();

    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    public void add(DataTypeValue value) {
        values.add(value);
    }

    public int size() {
        return values.size();
    }

    public int byteLength() {
        return values.stream().map(DataTypeValue::byteLength).reduce(0, (x, y) -> x + y);
    }

    @Override
    public Iterator<DataTypeValue> iterator() {
        return values.iterator();
    }

    public DataTypeValue get(int columnIndex) {
        return values.get(columnIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        return values.equals(record.values);

    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public static Record createRecord(String... stringValues) {
        Record record = new Record();
        for (String value : stringValues) {
            record.add(new VarcharValue(value));
        }
        return record;
    }
}
