package datamodel;

import java.io.Serializable;

/**
 * Created on 2014-10-21.
 */
public class Column implements Serializable {

    public static enum Nullable {NULLABLE, ISNOTNULL};

    public Identifier columnName;
    public SQLDataType dataType;

    public SQLDataType getDataType() {
        return dataType;
    }

    public Nullable nullable;

    public boolean isNullable() {
        return nullable == Nullable.NULLABLE;
    }

    public String columnComment;

    public Column(Identifier columnName, SQLDataType dataType, Nullable isNullable) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.nullable = isNullable;
    }

    public Column(Identifier columnName, SQLDataType dataType) {
        this(columnName, dataType, Nullable.NULLABLE);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (nullable != column.nullable) return false;
        if (columnComment != null ? !columnComment.equals(column.columnComment) : column.columnComment != null)
            return false;
        if (!columnName.equals(column.columnName)) return false;
        return dataType == column.dataType;

    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + nullable.hashCode();
        result = 31 * result + (columnComment != null ? columnComment.hashCode() : 0);
        return result;
    }
}
