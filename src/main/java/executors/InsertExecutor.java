package executors;

import clientapi.SQLException;
import datamodel.Identifier;
import datamodel.SQLDataType;
import datamodel.DataTypeValue;
import sqlparser.InsertCommand;
import sqlparser.AbstractSQLCommand;
import storageapi.DataStoreException;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2014-10-23.
 */
class InsertExecutor implements CommandExecutor {

    private Storage storage;
    private SystemDictionary dictionary;

    InsertExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    public int execute(AbstractSQLCommand command)  {
        InsertCommand insertCommand = (InsertCommand)command; // TODO should we catch possible ClassCastException and rethrow as SQLException? tather not as it would a programmer error to pass here command other than InsertComamnd
        Identifier tableName = insertCommand.getTableName();
        Collection<Identifier> tableColumnNamesList = dictionary.getTableColumnNames(tableName);
        List<Identifier> columnsSpecifiedInINSERT = insertCommand.getColumnList();
        List<String> valuesSpecifiedInINSERT = insertCommand.getValues();

        // 1. verify each column provided in INSERT column specification exists in the table to insert data
        for (Identifier column : columnsSpecifiedInINSERT) {
            if (!tableColumnNamesList.contains(column)) {
                throw new SQLException("Table " + tableName + " does not contain column " + column);
            }
        }

        List<DataTypeValue> valuesOfRecordToInsert = new LinkedList<>();

        if (columnsSpecifiedInINSERT.size() > 0)  { // column list specified
            for (Identifier column : tableColumnNamesList) {
                // check if all NOT NULL columns have been specified
                if ((dictionary.getColumnInfo(tableName, column).isNotNull) && !columnsSpecifiedInINSERT.contains(column)) {
                    throw new SQLException("Not null column " + column + " not specified in the INTO column specification clause");
                }
                // column was specified in insert list
                if (columnsSpecifiedInINSERT.contains(column)) {
                    int position = columnsSpecifiedInINSERT.indexOf(column);
                    SQLDataType columnDataType = dictionary.getColumnInfo(tableName, column).dataType;
                    DataTypeValue value = columnDataType.valueOf(valuesSpecifiedInINSERT.get(position));
                    valuesOfRecordToInsert.add(value);
                } else { // column not specified in insert list, but it is nullable columns so we insert null
                    valuesOfRecordToInsert.add(null);
                }
            }
        } else { // column list not specified, process value list along with column order in system dictionary
            // check if number of values provided equals number of columns of the table
            if (valuesSpecifiedInINSERT.size() != tableColumnNamesList.size()) {
                throw new SQLException("Number of values specified in INSERT does not match number of columns in the table " + tableName);
            }
            int columnIndex = 0;
            for (String stringValue : valuesSpecifiedInINSERT) {
                SQLDataType columnDataType = dictionary.getColumnInfo(tableName, columnIndex++).dataType;
                DataTypeValue value = columnDataType.valueOf(stringValue);
                valuesOfRecordToInsert.add(value);
            }
        }

        try {
            storage.insertRecord(tableName, valuesOfRecordToInsert);
        } catch (DataStoreException e) {
            throw new SQLException(e);
        }
        return 1;
    }

}
