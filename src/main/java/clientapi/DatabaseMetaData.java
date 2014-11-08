package clientapi;

import datamodel.Table;
import datamodel.VarcharValue;
import executors.IteratorBasedResultSet;
import systemdictionary.SystemDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 This is immutable view onto (some parts of) database system dictionary
 */
public class DatabaseMetaData {

    private static final String[] TABLE_METADATA_PSEUDOCOLUMNS = {"TABLE_NAME", "REMARKS"};
    private static final String[] COLUMN_METADATA_PSEUDOCOLUMNS = {"TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "NULLABLE", "REMARKS"};

    public static final List<String> tableMetadataPseudocolumnsList = Collections.unmodifiableList(Arrays.asList(TABLE_METADATA_PSEUDOCOLUMNS));

    DatabaseMetaData() {
    }

    /***
     * Each table description has the following columns
     *  TABLE_NAME String => table name
     *
     * @param tableNamePattern
     * @return
     */
    public ResultSet getTables(String tableNamePattern) {

        List<String> pseudoColumnsForTableView = new ArrayList<>();
        pseudoColumnsForTableView.addAll(Arrays.asList(TABLE_METADATA_PSEUDOCOLUMNS));

        SystemDictionary dictionary = SystemDictionary.getInstance();
        List<Table> tablesSelected = dictionary.getTables(tableNamePattern);
        List<List<VarcharValue>> tablesMetadata = new ArrayList<>();
        for (Table table : tablesSelected) {
            List<VarcharValue> tableRecord = new ArrayList<>();
            tableRecord.add(new VarcharValue(table.getTableName().getValue()));
            tableRecord.add(new VarcharValue(table.getTableComment()));
            tablesMetadata.add(tableRecord);
        }
        return new IteratorBasedResultSet<VarcharValue>(tablesMetadata.iterator(), pseudoColumnsForTableView, null);
    }


    /**
     * Each column description has the following columns:

     TABLE_NAME String => table name
     COLUMN_NAME String => column name
     DATA_TYPE int => SQL type from java.sql.Types
     TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
     COLUMN_SIZE int => column size.
     NULLABLE int => is NULL allowed.
     REMARKS String => comment describing column (may be null)

     * @param tableNamePattern
     * @param columnNamePattern
     * @return
     */
    public ResultSet getColumnsAsList(String tableNamePattern, String columnNamePattern) {
        List<String> pseudoColumnsForColumnsView = new ArrayList<>();
        pseudoColumnsForColumnsView.addAll(Arrays.asList(COLUMN_METADATA_PSEUDOCOLUMNS));
        //TODO
        return null;
    }
}
