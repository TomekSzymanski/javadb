package executors;

import datamodel.Identifier;
import sqlparser.AbstractSQLParser;
import sqlparser.SelectCommand;
import systemdictionary.SystemDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014-11-02.
 */
class QueryAssemblyFactory {

    private static SystemDictionary dictionary = SystemDictionary.getInstance();

    static QueryAssembly getInstance(SelectCommand selectSQLCommand, ExecutionContext context) {
        Identifier tableName = selectSQLCommand.getTableList().get(0);
        List<String> selectListElements = selectSQLCommand.getSelectList();
        List<Identifier> columnExpandedList = expandAsteriskIntoColumns(tableName, selectListElements);
        return new TableSelector(tableName, columnExpandedList, context);
    }

    private static List<Identifier> expandAsteriskIntoColumns(Identifier tableName, List<String> columnList) {
        List<Identifier> columnExpandedList = new ArrayList<>();
        if (isOnlyAsteriskSpecified(columnList)) {
            for (Identifier identifier : dictionary.getTableColumnNames(tableName)) {
                columnExpandedList.add(identifier);
            }
        } else {
            for (String column : columnList) {
                columnExpandedList.add(new Identifier(column));
            }
        }
        return columnExpandedList;
    }

    private static boolean isOnlyAsteriskSpecified(List<String> columnList) {
        return ((columnList.size() == 1) && (columnList.get(0).equals(AbstractSQLParser.ASTERISK)));
    }

}
