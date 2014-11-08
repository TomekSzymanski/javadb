package integrationtests;

import clientapi.Connection;
import clientapi.ResultSet;
import clientapi.Statement;

/**
 * Created on 2014-11-08.
 */
public class IntegrationTestsBase {

    boolean tableExists(Connection c, String tableName) {
        ResultSet rs = c.getMetaData().getTables(tableName);
        return (rs.next());
    }

    void createTestTable(Connection c, String tableName, String createTableSQL) {
        Statement stmt = c.createStatement();
        dropTestTable(c, tableName); // clear any old leftover, table will be recreated
        stmt.execute(createTableSQL);
    }

    void dropTestTable(Connection c, String tableName) {
        Statement stmt = c.createStatement();
        if (tableExists(c, tableName)) {
            stmt.execute("DROP TABLE " + tableName);
        }
    }
}
