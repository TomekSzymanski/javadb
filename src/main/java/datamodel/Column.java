package datamodel;

/**
 * Created on 2014-10-21.
 */
public class Column {

    public Identifier columnName;
    public SQLDataType dataType;
    public boolean isNotNull;
    public String columnComment;

    public Column(Identifier columnName, SQLDataType dataType, boolean isNotNull) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNotNull = isNotNull;
    }

    public Column(Identifier columnName, SQLDataType dataType) {

        this.columnName = columnName;
        this.dataType = dataType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (isNotNull != column.isNotNull) return false;
        if (columnComment != null ? !columnComment.equals(column.columnComment) : column.columnComment != null)
            return false;
        if (!columnName.equals(column.columnName)) return false;
        return dataType == column.dataType;

    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + (isNotNull ? 1 : 0);
        result = 31 * result + (columnComment != null ? columnComment.hashCode() : 0);
        return result;
    }

}
