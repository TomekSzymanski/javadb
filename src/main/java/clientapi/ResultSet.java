package clientapi;

/**
 * Created on 2014-11-02.
 */
public interface ResultSet {
    boolean next() throws SQLException;

    boolean getBoolean(String columnLabel) throws SQLException;

    boolean getBoolean(int columnIndex) throws SQLException;

    int getInt(String columnLabel) throws SQLException;

    int getInt(int columnIndex) throws SQLException;

    String getString(String columnLabel) throws SQLException;

    String getString(int columnIndex) throws SQLException;

    int findColumn(String columnLabel);
}
