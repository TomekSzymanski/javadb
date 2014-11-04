package systemdictionary;

import datamodel.Column;
import datamodel.Identifier;
import datamodel.Table;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2014-10-21.
 */
public class SystemDictionary {

    private static SystemDictionary INSTANCE;

    public static SystemDictionary getInstance() {
        if (INSTANCE==null) {
            INSTANCE = new SystemDictionary(); // TODO: load dictionary from disk
        }
        return INSTANCE;
    }

    private SystemDictionary() {}

    private Map<Identifier, Table> registeredTables = new HashMap<>();

    public void registerTable(Table table) throws DataDictionaryException {
        if (registeredTables.containsKey(table.getTableName())) {
            throw new DataDictionaryException("Table " + table.getTableName() + " already exists");
        } else {
            registeredTables.put(table.getTableName(), table);
        }
    }


    public Collection<Identifier> getTableColumnNames(Identifier tableName) {
        return registeredTables.get(tableName).getColumnNames();
    }

    public List<Column> getTableColumnsAsList(Identifier tableName) {
        return registeredTables.get(tableName).getColumnsAsList();
    }

    public Column getColumnInfo(Identifier tableName, Identifier columnName) {
        return registeredTables.get(tableName).getColumn(columnName);
    }

    /**
     * Returns table Column that stands on index position, starting from 0
     * @param tableName
     * @param columnPosition
     * @return
     */
    public Column getColumnInfo(Identifier tableName, int index) {
        return registeredTables.get(tableName).getColumn(index);
    }

    public void deregisterTable(Identifier tableName) throws DataDictionaryException {
        if (!registeredTables.containsKey(tableName)) {
            throw new DataDictionaryException("Table " + tableName.getValue() + " does not exist");
        } else {
            registeredTables.remove(tableName);
        }
    }
}
