package executors;

import clientapi.SQLException;
import datamodel.DataTypeValue;
import datamodel.Identifier;
import datamodel.NullValue;
import datamodel.SQLDataType;
import sqlparser.InsertCommand;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2014-10-23.
 */
class InsertExecutor implements CommandExecutor<InsertCommand> {

    private Storage storage;
    private SystemDictionary dictionary;

    InsertExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    // TODO extract short methods
    public void execute(InsertCommand command)  {
        Identifier tableName = command.getTableName();
        if (!dictionary.tableExists(tableName)) {
            throw new SQLException("Trying to insert to non existent table " + tableName);
        }
        Collection<Identifier> tableColumnNamesList = dictionary.getTableColumnNames(tableName);
        List<Identifier> columnsSpecifiedInINSERT = command.getColumnList();
        List<String> valuesSpecifiedInINSERT = command.getValues();

        // 1. verify each column provided in INSERT column specification exists in the table to insert data
        ArrayList<Identifier> invalidColumns = new ArrayList<>(columnsSpecifiedInINSERT);
        invalidColumns.removeAll(tableColumnNamesList);
        if (!invalidColumns.isEmpty()) {
            throw new SQLException("Table " + tableName + " does not contain columns " + invalidColumns);
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
                } else { // column not specified in insert list, but it is nullable columns so we insert SQL null value
                    valuesOfRecordToInsert.add(NullValue.NULL);
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
        storage.insertRecord(tableName, valuesOfRecordToInsert);

    }

}
