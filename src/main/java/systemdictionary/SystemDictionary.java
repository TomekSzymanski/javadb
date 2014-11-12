package systemdictionary;

import clientapi.ResultSet;
import datamodel.*;
import executors.IteratorBasedResultSet;

import java.util.*;
import java.util.regex.Pattern;

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

    public boolean tableExists(Identifier tableName) {
        return registeredTables.containsKey(tableName);
    }


    /**
     * Returns unmodifiable view into database metadata. View gets updates when metadate in DB get modified (by DDL for example), and you DO NOT need to reload it again to have it up to date
     * @return
     */
    public DatabaseMetaDataView getDatabaseMetaData() {
        return new DatabaseMetaDataView();
    }

    /**
     This is immutable view onto (some parts of) database system dictionary
     */
    private class DatabaseMetaDataView implements clientapi.DatabaseMetaData {

        private final String[] TABLE_METADATA_PSEUDOCOLUMNS = {"TABLE_NAME", "REMARKS"};
        private final String[] COLUMN_METADATA_PSEUDOCOLUMNS = {"TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "COLUMN_SIZE", "NULLABLE", "REMARKS"};

        @Override
        public final List<String> getTableMetadataPseudocolumnsList() {
            return Collections.unmodifiableList(Arrays.asList(TABLE_METADATA_PSEUDOCOLUMNS));
        }

        @Override
        public final List<String> getColumnMetadataPseudocolumnsList() {
            return Collections.unmodifiableList(Arrays.asList(COLUMN_METADATA_PSEUDOCOLUMNS));
        }


        private List<List<DataTypeValue>> getTablesMetadata(String tableNameRegex) {
            List<List<DataTypeValue>> tablesMetadata = new ArrayList<>();
            for (Table table : registeredTables.values()) {
                Identifier tableName = table.getTableName();
                if (Pattern.matches(tableNameRegex, tableName.getValue())) {
                    List<DataTypeValue> tableMetadataRecord = new ArrayList<>();
                    tableMetadataRecord.add(new VarcharValue(tableName.getValue()));
                    tableMetadataRecord.add(new VarcharValue(table.getTableComment()));
                    tablesMetadata.add(tableMetadataRecord);
                }
            }
            return tablesMetadata;
        }

        private List<List<DataTypeValue>> getColumnsMetadata(String tableNameRegex, String columnNameRegex) {
            List<List<DataTypeValue>> columnsMetadata = new ArrayList<>();
            for (Table table : registeredTables.values()) {
                Identifier tableName = table.getTableName();
                if (Pattern.matches(tableNameRegex, tableName.getValue())) {
                    for (Column column : table.getColumnsAsList()) {
                        if (Pattern.matches(columnNameRegex, column.columnName.getValue())) {
                            List<DataTypeValue> columnMetadataRecord = new ArrayList<>();
                            columnMetadataRecord.add(new VarcharValue(tableName.getValue())); // TABLE_NAME
                            columnMetadataRecord.add(new VarcharValue(column.columnName.getValue())); // COLUMN_NAME
                            columnMetadataRecord.add(new VarcharValue(column.dataType.getTypeName())); // DATA_TYPE
                            columnMetadataRecord.add(new IntegerValue(column.dataType.getFieldSizeSpecifier(0))); // COLUMN_SIZE
                            columnMetadataRecord.add(BooleanValue.valueOf(!column.isNotNull)); // NULLABLE
                            columnMetadataRecord.add(new VarcharValue(column.columnComment)); // REMARKS
                            columnsMetadata.add(columnMetadataRecord);
                        }
                    }
                }
            }
            return columnsMetadata;
        }

        /***
         * Each table description has the following columns
         *  TABLE_NAME String => table name
         *
         * @param tableNamePattern
         * @return
         */
        @Override
        public ResultSet getTables(String tableNamePattern) {
            return new IteratorBasedResultSet<>(getTablesMetadata(tableNamePattern).iterator(), getTableMetadataPseudocolumnsList());
        }

        /**
         * Each column description has the following columns:

         TABLE_NAME String => table name
         COLUMN_NAME String => column name
         DATA_TYPE String => SQL type from java.sql.Types
         COLUMN_SIZE int => column size.
         NULLABLE boolean => is NULL allowed.
         REMARKS String => comment describing column (may be null)

         * @param tableNamePattern
         * @param columnNamePattern
         * @return
         */
        @Override
        public ResultSet getColumns(String tableNamePattern, String columnNamePattern) {
            return new IteratorBasedResultSet<>(getColumnsMetadata(tableNamePattern, columnNamePattern).iterator(), getColumnMetadataPseudocolumnsList());
        }
    }
}
