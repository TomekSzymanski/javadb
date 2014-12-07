package integrationtests;

import clientapi.Connection;
import clientapi.DatabaseBuilder;
import clientapi.JavaDB;
import clientapi.Statement;
import org.junit.AfterClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created on 2014-11-07.
 */
public class CreateInsertAndDeleteAllTest extends IntegrationTestsBase {

    private static JavaDB database = new DatabaseBuilder().newInMemoryDatabase().build();
    private static Connection c = database.getConnection();
    private static final String TEST_TABLE_NAME = "CreateInsertAndDeleteAllTest";


    @Test
    public void testInsertAndDeleteAll() throws SQLException {
        // given
        try {
            createTestTable(c, TEST_TABLE_NAME, "CREATE TABLE " + TEST_TABLE_NAME + " (a NUMBER NOT NULL, b NUMBER)");

            Statement stmt = c.createStatement();

            // when you specify INSERT not specifying nullable column
            stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (1)");
            stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (2)");
            stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (3)");

            clientapi.ResultSet rs = stmt.executeQuery("SELECT * FROM " + TEST_TABLE_NAME);

            // then
            // 1. there are 3 records in the table:
            assertTrue(rs.next());
            assertTrue(rs.next());
            assertTrue(rs.next());

            //now delete all records
            stmt.execute("DELETE FROM " + TEST_TABLE_NAME);

            // check table is empty:
            rs = stmt.executeQuery("SELECT * FROM " + TEST_TABLE_NAME);
            assertFalse(rs.next());

        } catch (clientapi.SQLException e) {
            fail(e.getMessage());
        }
        finally {
            // tear down
            dropTestTable(c, TEST_TABLE_NAME);
        }
    }

    @AfterClass
    public static void tearDown() {
        database.close();
    }

}
