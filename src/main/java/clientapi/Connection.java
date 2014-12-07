package clientapi;

import systemdictionary.SystemDictionary;

/**
 A connection (session) with a specific database. SQL statements are executed and results are returned within the context of a connection.
 */
public class Connection implements AutoCloseable {

    private JavaDB database;

    Connection(JavaDB database) {
        this.database = database;
    };

    /**
     * Creates a Statement object for sending SQL statements to the database.
     * @return
     */
    public Statement createStatement() {
        return new Statement(database.getCommandExecutorFactory());
    }

    /**
     * Returns reference to database metadata (information about tables, columns)
     * @return
     * @throws SQLException
     */
    public DatabaseMetaData getMetaData() {
        SystemDictionary dictionary = database.getSystemDictionary();
        return dictionary.getDatabaseMetaData();
    }


    /**
     * Releases this Connection object's database and JDBC resources immediately instead of waiting for them to be automatically released.
     * @throws Exception
     */
    @Override
    public void close() throws SQLException {
    }
}
