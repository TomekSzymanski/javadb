package clientapi;

/**
 A connection (session) with a specific database. SQL statements are executed and results are returned within the context of a connection.
 */
public class Connection implements AutoCloseable {

    /**
     * Creates a Statement object for sending SQL statements to the database.
     * @return
     */
    public Statement createStatement() {
        return new Statement();
    }

    /**
     * Returns reference to database metadata (information about tables, columns)
     * @return
     * @throws SQLException
     */
    public DatabaseMetaData getMetaData() {
//        return SystemDictionary.getInstance().getDatabaseMetaData();
        return null;
    }

    /**
     * Releases this Connection object's database and JDBC resources immediately instead of waiting for them to be automatically released.
     * @throws Exception
     */
    @Override
    public void close() throws Exception {

    }
}
