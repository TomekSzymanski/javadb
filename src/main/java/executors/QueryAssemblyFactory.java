package executors;

import datamodel.Identifier;
import sqlparser.AbstractSQLParser;
import sqlparser.SelectCommand;
import systemdictionary.SystemDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2014-11-02.
 */
class QueryAssemblyFactory {

    private static SystemDictionary dictionary = SystemDictionary.getInstance();

    static QueryAssembly getInstance(SelectCommand selectSQLCommand) {
        Identifier tableName = selectSQLCommand.getTableList().get(0);
        List<Identifier> columnList = selectSQLCommand.getColumnList();
        List<Identifier> columnExpandedList;
        if (isOnlyAsteriskSpecified(columnList)) {
            columnExpandedList = new ArrayList<>();
            for (Iterator<Identifier> it = dictionary.getTableColumnNames(tableName).iterator(); it.hasNext(); ) {
                columnExpandedList.add(it.next());
            }
        } else {
            columnExpandedList = columnList;
        }
        return new TableSelector(tableName, columnExpandedList);
    }

    private static boolean isOnlyAsteriskSpecified(List<Identifier> columnList) {
        return ((columnList.size() == 1) && (columnList.get(0).equals(AbstractSQLParser.ASTERISK)));
    }


}
