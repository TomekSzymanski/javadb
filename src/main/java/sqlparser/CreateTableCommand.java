package sqlparser;

import datamodel.Column;
import datamodel.Identifier;
import datamodel.Table;

import java.util.List;

/**
 * Created on 2014-10-21.
 */
public class CreateTableCommand extends AbstractSQLCommand {

    public CreateTableCommand() {
        super(Type.CreateTableCommand);
    }

    private Table table;

    public List<Column> getColumnsAsList() {
        return table.getColumnsAsList();
    }

    public Table getTableInfo() {
        return table;
    }

    void setTableName(Identifier tableName) {
        this.table = new Table(tableName);
    }

    void addColumnInfo(Column column) {
        table.addColumn(column);
    }
}
