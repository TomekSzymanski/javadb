package executors;

import datamodel.Identifier;
import sqlparser.AbstractSQLParser;
import sqlparser.SelectCommand;
import systemdictionary.SystemDictionary;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Identifier> columnExpandedList;
        if (isOnlyAsteriskSpecified(columnList)) {
            columnExpandedList = dictionary.getTableColumnNames(tableName).stream().collect(Collectors.toList());
        } else {
            columnExpandedList = columnList.stream().map(Identifier::new).collect(Collectors.toList());
        }
        return columnExpandedList;
    }

    private static boolean isOnlyAsteriskSpecified(List<String> columnList) {
        return ((columnList.size() == 1) && (columnList.get(0).equals(AbstractSQLParser.ASTERISK)));
    }

}
