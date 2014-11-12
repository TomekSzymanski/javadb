package clientapi;

import java.util.List;

/**
 * Created on 2014-11-12.
 */
public interface DatabaseMetaData {
    List<String> getTableMetadataPseudocolumnsList();

    List<String> getColumnMetadataPseudocolumnsList();

    ResultSet getTables(String tableNamePattern);

    ResultSet getColumns(String tableNamePattern, String columnNamePattern);
}
