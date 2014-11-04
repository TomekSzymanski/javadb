package integrationtests;

import clientapi.Connection;
import clientapi.Statement;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2014-11-03.
 */
public class Insert {

    private static Statement stmt;

    @BeforeClass
    public static void setUp() {
        Connection c = new Connection();
        stmt = c.createStatement();
    }
    @Test
    public void testInsertNullableColumnsNotSpecified() throws SQLException {
        // given
        final String TEST_TABLE_NAME = "InsertNullableColumns";
        try {


            stmt.execute("CREATE TABLE " + TEST_TABLE_NAME + " (a NUMBER NOT NULL, b NUMBER)");

            // when you specify INSERT not specifying nullable column
            stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (1)");

            // then nullable, not specifed columns, will get NULL value
            clientapi.ResultSet rs = stmt.executeQuery("SELECT * FROM " + TEST_TABLE_NAME);
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("a"));
            assertEquals(null, rs.getString("b"));
            assertEquals(0, rs.getInt("b"));
            // TODO assertEquals(0, rs.getFloat("b"), 0);
            assertEquals(false, rs.getBoolean("b"));
        } finally {
            // tear down
            stmt.execute("DROP TABLE " + TEST_TABLE_NAME);
        }
    }
}
