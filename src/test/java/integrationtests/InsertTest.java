package integrationtests;

import clientapi.Connection;
import clientapi.DatabaseBuilder;
import clientapi.JavaDB;
import clientapi.Statement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created on 2014-11-03.
 */
public class InsertTest extends IntegrationTestsBase {

    private static JavaDB database = new DatabaseBuilder().newInMemoryDatabase().build();
    private static Connection c = database.getConnection();
    private static Statement stmt;

    @BeforeClass
    public static void setUp() {
        stmt = c.createStatement();
    }

    @Test
    public void testInvalidColumnsProvided() {
        final String TEST_TAB_NAME = "testInvalidColumnsProvided";
        // given: a table with two columns is created
        IntegrationTestsBase.createTestTable(c, TEST_TAB_NAME,
                "CREATE TABLE " + TEST_TAB_NAME + " (a varchar(100), b number(10))");

        // inserting specifying existing columns should work
        c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a, b) VALUES ('tttt', 1330)");

        // when trying to specify non-existent column in INSERT
        try {
            c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a, x) VALUES ('tttt', 1330)");
            fail("SQLException should have been thrown");
        } catch (clientapi.SQLException e) {}
    }

    @Test
    public void notNullColumnNotSpecifiedInInsert() {
        final String TEST_TAB_NAME = "notNullColumnNotSpecifiedInInsert";
        // given: a table with two columns is created
        IntegrationTestsBase.createTestTable(c, TEST_TAB_NAME,
                "CREATE TABLE " + TEST_TAB_NAME + " (a varchar(100), b number(10) NOT NULL)");

        // inserting specifying not null column should work
        c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a, b) VALUES ('tttt', 1330)");

        // when trying to insert without specifying non-null column in insert list:
        try {
            c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a) VALUES ('tttt')");
            fail("SQLException should have been thrown");
        } catch (clientapi.SQLException e) {}
    }

    @Test
    public void testInsertNullableColumnsNotSpecified() throws SQLException {
        // given
        final String TEST_TABLE_NAME = "testInsertNullableColumnsNotSpecified";
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
        } finally {
            dropTestTable(c, TEST_TABLE_NAME);
        }
    }

    @Test (expected = clientapi.SQLException.class)
    public void tryToInsertIntoNonExistentTable() {
        final String TEST_TABLE_NAME = "NON_EXISTENT_TABLE34534";
        stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a) VALUES (1)");
    }

    @Test
    public void specifyingNullInValuesList() {
        // given
        final String TEST_TABLE_NAME = "specifyingNullInValuesList";
        try {
            // given: a table with 3 columns is created
            createTestTable(c, TEST_TABLE_NAME, "CREATE TABLE " + TEST_TABLE_NAME + " (a varchar(100), b number(10), c varchar(10) )");

            // when we do insert, specifying NULL as one of the values
            stmt.execute("INSERT INTO " + TEST_TABLE_NAME + " (a, b, c) VALUES ('tttt', NULL, 'X')");

            // then NULL is understood and correctly inserted. All other values are corrctly inserted
            clientapi.ResultSet rs = stmt.executeQuery("SELECT * FROM " + TEST_TABLE_NAME);
            assertTrue(rs.next());
            assertEquals("tttt", rs.getString("a"));
            assertEquals(0, rs.getInt("b"));
            assertEquals("X", rs.getString("c"));
        } finally {
            dropTestTable(c, TEST_TABLE_NAME);
        }

    }

    @AfterClass
    public static void tearDown() {
        database.close();
    }
}
