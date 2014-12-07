package executors;

import clientapi.SQLException;
import datamodel.DataTypeValue;
import datamodel.Identifier;
import datamodel.NullValue;
import datamodel.SQLDataType;
import sqlparser.InsertCommand;
import storageapi.Record;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2014-10-23.
 */
class InsertExecutor implements CommandExecutor<InsertCommand> {

    private Storage storage;
    private SystemDictionary dictionary; //TODO enough to have in many places only view on dictionary (unmodifiable)

    InsertExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    public void execute(InsertCommand command)  {
        Identifier tableName = command.getTableName();

        if (!dictionary.tableExists(tableName)) {
            throw new SQLException("Trying to insert to non existent table " + tableName);
        }

        List<Identifier> columnsSpecifiedInInsert = command.getColumnList();
        List<String> valuesSpecifiedInInsert = command.getValues();

        Record valuesOfRecordToInsert;

        if (!columnsSpecifiedInInsert.isEmpty())  { // column list specified
            /* 1. verify each column provided in INSERT column specification exists in the table to insert data */
            validateAllColumnsExistInTable(tableName, columnsSpecifiedInInsert);

            /* 2. check if all NOT NULL columns have been specified */
            validateAllNotNullColumnsSpecified(tableName, columnsSpecifiedInInsert);

            valuesOfRecordToInsert = createRecordValues(tableName, valuesSpecifiedInInsert, columnsSpecifiedInInsert);

        } else { // column list not specified, process value list along with column order in system dictionary
            // check if number of values provided equals number of columns of the table
            validateAllColumnsSpecified(tableName, valuesSpecifiedInInsert);

            valuesOfRecordToInsert = createRecordValues(tableName, valuesSpecifiedInInsert);

        }
        storage.insertRecord(tableName, valuesOfRecordToInsert);
    }

    private Record createRecordValues(Identifier tableName, List<String> valuesSpecifiedInInsert, List<Identifier> columnsSpecifiedInInsert) {
        Record recordValues = new Record();
        Collection<Identifier> tableColumnNamesList = dictionary.getTableColumnNames(tableName);
        for (Identifier column : tableColumnNamesList) {
            if (columnsSpecifiedInInsert.contains(column)) { // column was specified in insert list
                int position = columnsSpecifiedInInsert.indexOf(column);
                SQLDataType columnDataTypeFactory = dictionary.getColumnInfo(tableName, column).dataType;
                DataTypeValue value = columnDataTypeFactory.valueOf(valuesSpecifiedInInsert.get(position));
                recordValues.add(value);
            } else { // column not specified in insert list, but it is nullable columns (ckecked earlier) so we insert NULL value
                recordValues.add(NullValue.NULL);
            }
        }
        return recordValues;
    }

    private Record createRecordValues(Identifier tableName, List<String> valuesSpecifiedInInsert) {
        Record recordValues = new Record();
        int columnIndex = 0;
        for (String stringValue : valuesSpecifiedInInsert) {
            SQLDataType columnDataTypeFactory = dictionary.getColumnInfo(tableName, columnIndex++).dataType;
            DataTypeValue value = columnDataTypeFactory.valueOf(stringValue);
            recordValues.add(value);
        }
        return recordValues;
    }

    private void validateAllColumnsSpecified(Identifier tableName, List<String> valuesSpecifiedInInsert) {
        int numberOfColumnsInTable = dictionary.getTableColumnNames(tableName).size();
        if (valuesSpecifiedInInsert.size() != numberOfColumnsInTable) {
            throw new SQLException("Number of values specified in INSERT does not match number of columns of the table " + tableName + ". Need to specify " + numberOfColumnsInTable + " values");
        }
    }

    private void validateAllNotNullColumnsSpecified(Identifier tableName, List<Identifier> columnsSpecifiedInInsert) {
        List<Identifier> notSpecifiedNotNullColumns = dictionary.getTableColumnNames(tableName).stream()
                .filter(column -> !dictionary.getColumnInfo(tableName, column).isNullable())
                .filter(column -> !columnsSpecifiedInInsert.contains(column)).collect(Collectors.toList());
        if (!notSpecifiedNotNullColumns.isEmpty()) {
            throw new SQLException("Not null columns " + notSpecifiedNotNullColumns + " not specified in column specification clause");
        }
    }

    private void validateAllColumnsExistInTable(Identifier tableName, List<Identifier> columnsSpecifiedInInsert) {
        List<Identifier> invalidColumns = new ArrayList<>(columnsSpecifiedInInsert);
        invalidColumns.removeAll(dictionary.getTableColumnNames(tableName));
        if (!invalidColumns.isEmpty()) {
            throw new SQLException("Table " + tableName + " does not contain columns " + invalidColumns);
        }
    }


}
