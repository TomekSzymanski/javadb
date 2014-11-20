package datamodel;

import org.apache.commons.lang3.Validate;

import java.util.*;

/**
 * Created on 2014-10-21.
 */
public class Table {

    private Identifier tableName;

    private Map<Identifier, Column> columnMap = new LinkedHashMap<>();

    private List<Identifier> primaryKey = new ArrayList<>(); // list of columnMap making primary key

    public String getTableComment() {
        return tableComment;
    }

    private String tableComment;

    public Table(Identifier tableName) {
        this.tableName = tableName;
    }

    public Table(String tableNameString) {
        this(new Identifier(tableNameString));
    }

    public Column getColumn(int index) {
        return (Column)columnMap.values().toArray()[index];
    }

    public Column getColumn(Identifier columnName) {
        Column info = columnMap.get(columnName);
        Validate.notNull(info, "Invalid column name %s for table %s", columnName, tableName);
        return info;
    }

    /**
     * Returns columnMap in the order there were specified when creating the table
     * @return
     */
    public List<Column> getColumnsAsList() {
        return Collections.unmodifiableList(new ArrayList<>(columnMap.values()));
    }

    public Collection<Identifier> getColumnNames() {
        return columnMap.keySet();
    }

    public Identifier getTableName() {
        return tableName;
    }

    public void setTableName(Identifier tableName) {
        this.tableName = tableName;
    }

    public void addColumn(Column column) {
        Identifier columnName = column.columnName;
        Validate.isTrue(!columnMap.containsKey(columnName), "Table '%s' already contains column '%s'", tableName, columnName);
        columnMap.put(columnName, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (!columnMap.equals(table.columnMap)) return false;
        if (primaryKey != null ? !primaryKey.equals(table.primaryKey) : table.primaryKey != null) return false;
        if (tableComment != null ? !tableComment.equals(table.tableComment) : table.tableComment != null) return false;
        return tableName.equals(table.tableName);

    }

    @Override
    public int hashCode() {
        int result = tableName.hashCode();
        result = 31 * result + columnMap.hashCode();
        result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
        result = 31 * result + (tableComment != null ? tableComment.hashCode() : 0);
        return result;
    }
}
