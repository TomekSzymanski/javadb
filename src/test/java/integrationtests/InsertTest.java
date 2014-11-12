package integrationtests;

import clientapi.Connection;
import clientapi.Statement;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created on 2014-11-03.
 */
public class InsertTest extends IntegrationTestsBase {

    private static final Connection c = new Connection();
    private static Statement stmt;

    @BeforeClass
    public static void setUp() {
        stmt = c.createStatement();
    }
    @Test
    public void testInsertNullableColumnsNotSpecified() throws SQLException {
        // given
        final String TEST_TABLE_NAME = "InsertNullableColumns";
        try {
            createTestTable(c, TEST_TABLE_NAME, "CREATE TABLE " + TEST_TABLE_NAME + " (a NUMBER NOT NULL, b NUMBER)");

            // when you specify INSERT not specifying nullable column
            stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (1)");

            // then nullable, not specifed columns, will get NULL value
            clientapi.ResultSet rs = stmt.executeQuery("SELECT * FROM " + TEST_TABLE_NAME);
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("a"));
            assertEquals(null, rs.getString("b"));
            assertEquals(0, rs.getInt("b"));
            assertEquals(false, rs.getBoolean("b"));
        }catch (clientapi.SQLException e) {
            dropTestTable(c, TEST_TABLE_NAME);
            fail(e.getMessage());
        }
    }

    @Test (expected = clientapi.SQLException.class)
    public void tryToInsertIntoNonExistentTable() {
        final String TEST_TABLE_NAME = "NON_EXISTENT_TABLE34534";
        stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (1)");
    }
}
