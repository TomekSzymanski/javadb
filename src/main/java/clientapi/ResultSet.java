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

    /**
     * Retrieves the value of the designated column in the current row of this ResultSet object as a long in the Java programming language.
     * the column value; if the value is SQL NULL, the value returned is 0
     */
    long getLong(int columnIndex) throws SQLException;

    long getLong(String columnLabel) throws SQLException;

    String getString(String columnLabel) throws SQLException;

    String getString(int columnIndex) throws SQLException;

    int findColumn(String columnLabel);
}
