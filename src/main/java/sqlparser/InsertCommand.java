package sqlparser;

import datamodel.Identifier;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014-10-22.
 */
public class InsertCommand extends AbstractSQLCommand {

    @Override
    public Type getType() {
        return Type.InsertCommand;
    }

    private Identifier tableName;
    private List<Identifier> columnList = new ArrayList<>(); // columnList may be empty

    private List<String> values = new ArrayList<>(); // values list cannot be empty

    public Identifier getTableName() {
        return tableName;
    }

    void setTableName(Identifier tableName) {
        this.tableName = tableName;
    }

    public List<Identifier> getColumnList() {
        return columnList;
    }

    public List<String> getValues() {
        return values;
    }

    void addColumn(Identifier columnName) {
        columnList.add(columnName);
    }

    void setColumnList(List<Identifier> columnList) {
        this.columnList = columnList;
    }

    void setValuesList(List<String> values) {
        Validate.isTrue((columnList.isEmpty()) || (values.size() <= columnList.size()), "If column list is specified then you cannot add more values than columns");
        this.values = values;
    }

    /**
     * Either columnList is empty and we add any values
     * OR we have already added columns to columnList and we check if number of values being added does not exceed number of columns on column list
     * @param value
     */
    void addValue(String value) {
        Validate.isTrue((columnList.isEmpty()) || (values.size() <= columnList.size()), "If column list is specified then you cannot add more values than columns");
        values.add(value);
    }
}
