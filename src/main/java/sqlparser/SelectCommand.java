package sqlparser;

import datamodel.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014-10-04.
 */
public class SelectCommand extends AbstractSQLCommand {

    public SelectCommand() {
        super(Type.SelectCommand);
    }

    private final List<String> selectList = new ArrayList<>(); // it cannot be Identifier list, as in the column specification we may have columns (Identifiers), but also strings or asterisk
    private final List<Identifier> tableList = new ArrayList<>();

    //private WhereClauseSqlAST whereConditions;

    void addSelectListElement(String columnName) {
        selectList.add(columnName);
    }

    void addSelectListElements(List<String> identifiers) {
        selectList.addAll(identifiers);
    }

    void addTable(Identifier tableName) {
        tableList.add(tableName);
    }

    void addTables(List<Identifier> identifiers) {
        tableList.addAll(identifiers);
    }

    public List<String> getSelectList() {
        return selectList;
    }

    public List<Identifier> getTableList() {
        return tableList;
    }
}
