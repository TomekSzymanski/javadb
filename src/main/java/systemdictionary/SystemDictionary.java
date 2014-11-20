package systemdictionary;

import clientapi.DatabaseMetaData;
import clientapi.ResultSet;
import datamodel.*;
import executors.IteratorBasedResultSet;
import storageapi.DataStoreException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

/**
 * Created on 2014-10-21.
 */
public class SystemDictionary {

    private static SystemDictionary INSTANCE;

    public synchronized static SystemDictionary getInstance() {
        if (INSTANCE==null) {
            throw new IllegalStateException("trying to get reference to not initialized dictionary");
        }
        return INSTANCE;
    }

    public synchronized static SystemDictionary createEmptyDictionary() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Trying to initialize already initialized system dictionary");
        }
        INSTANCE = new SystemDictionary();
        return INSTANCE;
    }

    /**
     * Loads system dictionary from storage
     */
    public synchronized static SystemDictionary loadSystemDictionary(File systemDictionaryFile) {
        if (INSTANCE != null) {
            throw new IllegalStateException("Trying to initialize already initialized system dictionary");
        }

        if (systemDictionaryFile.length() == 0) { // new database created, have to initialize itself as empty registry
            INSTANCE = new SystemDictionary();
        } else { // system dictionary file not empty -> boot itself from that file
            try { // TODO: too many try? (first is try-with-resources)
                try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(systemDictionaryFile))) {
                    INSTANCE = (SystemDictionary) input.readObject();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new DataStoreException(e);
            }
        }
        return INSTANCE;
    }

    private SystemDictionary() {}

    private Map<Identifier, Table> registeredTables = new HashMap<>();

    private ReadWriteLock tablesLock = new ReentrantReadWriteLock();

    public void registerTable(Table table) throws DataDictionaryException {
        tablesLock.writeLock().lock();
        if (registeredTables.containsKey(table.getTableName())) {
            tablesLock.writeLock().unlock();
            throw new DataDictionaryException("Table " + table.getTableName() + " already exists"); //TODO add persisting dictionary state to the file
        }
        registeredTables.put(table.getTableName(), table);
        tablesLock.writeLock().unlock();
    }

    public void deregisterTable(Identifier tableName) throws DataDictionaryException {
        tablesLock.writeLock().lock();
        if (!registeredTables.containsKey(tableName)) {
            tablesLock.writeLock().unlock();
            throw new DataDictionaryException("Table " + tableName.getValue() + " does not exist");
        }
        registeredTables.remove(tableName);
        tablesLock.writeLock().unlock();
    }

    public Collection<Identifier> getTableColumnNames(Identifier tableName) {
        tablesLock.readLock().lock();
        Collection<Identifier> tableColumnNames = registeredTables.get(tableName).getColumnNames();
        tablesLock.readLock().unlock();
        return tableColumnNames;
    }

    public List<Column> getTableColumnsAsList(Identifier tableName) {
        tablesLock.readLock().lock();
        List<Column> tableColumnsList = registeredTables.get(tableName).getColumnsAsList();
        tablesLock.readLock().unlock();
        return tableColumnsList;
    }

    public Column getColumnInfo(Identifier tableName, Identifier columnName) {
        tablesLock.readLock().lock();
        Column column = registeredTables.get(tableName).getColumn(columnName);
        tablesLock.readLock().unlock();
        return column;
    }

    /**
     * Returns table Column that stands on index position, starting from 0
     * @param tableName
     * @return
     */
    public Column getColumnInfo(Identifier tableName, int index) {
        tablesLock.readLock().lock();
        Column column = registeredTables.get(tableName).getColumn(index);
        tablesLock.readLock().unlock();
        return column;

    }


    public boolean tableExists(Identifier tableName) {
        tablesLock.readLock().lock();
        boolean exists = registeredTables.containsKey(tableName);
        tablesLock.readLock().unlock();
        return exists;
    }

    /**
     * Returns unmodifiable view into database metadata. View gets updates when metadate in DB get modified (by DDL for example), and you DO NOT need to reload it again to have it up to date
     * @return
     */
    public DatabaseMetaDataView getDatabaseMetaData() {
        tablesLock.readLock().lock();
        DatabaseMetaDataView metaDataView = new DatabaseMetaDataView();
        tablesLock.readLock().unlock();
        return metaDataView;
    }

    /**
     This is immutable view onto (some parts of) database system dictionary
     */
    private class DatabaseMetaDataView implements DatabaseMetaData {

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
