package sqlparser;

import datamodel.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014-10-04.
 */
public class SelectCommand extends AbstractSQLCommand {

    @Override
    public Type getType() {
        return Type.SelectCommand;
    }
    private final List<Identifier> columnList = new ArrayList<>();
    private final List<Identifier> tableList = new ArrayList<>();

    //private WhereClauseSqlAST whereConditions;

    void addColumn(Identifier columnName) {
        columnList.add(columnName);
    }

    void addColumns(List<Identifier> identifiers) {
        for (Identifier identifier : identifiers) {
            addColumn(identifier);
        }
    }

    void addTable(Identifier tableName) {
        tableList.add(tableName);
    }

    void addTables(List<Identifier> identifiers) {
        for (Identifier identifier : identifiers) {
            addTable(identifier);
        }
    }

    public List<Identifier> getColumnList() {
        return columnList;
    }


    public List<Identifier> getTableList() {
        return tableList;
    }
}
