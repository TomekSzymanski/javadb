package clientapi;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2014-11-08.
 *
 * Annotation @VisibleForTesting - mark package methods use by tests only
 */
public class DatabaseMetaDataTest {

    private static JavaDB database = new DatabaseBuilder().newInMemoryDatabase().build();
    private static Connection c = database.getConnection();

    @Test
    public void getTablesByPattern() {

        Statement stmt = c.createStatement();
        // given: create 2 tables
        stmt.execute("CREATE TABLE TABAAAA111 (id NUMBER)");
        stmt.execute("CREATE TABLE TABAAAA122 (id NUMBER)");
        stmt.execute("CREATE TABLE TABXXX (id NUMBER)");

        // when metadata selected
        DatabaseMetaData metadata = c.getMetaData();
        ResultSet rs = metadata.getTables("TABA.*");

        // then
        assertTrue(rs.next());
        assertEquals("TABAAAA111", rs.getString(0));
        assertEquals("TABAAAA111", rs.getString("TABLE_NAME"));

        assertTrue(rs.next());
        assertEquals("TABAAAA122", rs.getString(0));
        assertEquals("TABAAAA122", rs.getString("TABLE_NAME"));

        assertFalse(rs.next());
    }

    @Test
    public void getColumnsByPattern() {
        Statement stmt = c.createStatement();

        // given: create 2 tables
        stmt.execute("CREATE TABLE products (id NUMBER, name VARCHAR(20))");
        stmt.execute("CREATE TABLE customers (id NUMBER(4) NOT NULL, level VARCHAR(1), sales_amount NUMBER(8,2))");
        stmt.execute("CREATE TABLE customers_import (id_x NUMBER(4), identifier VARCHAR(25), name VARCHAR(255))");

        // when metadata selected
        DatabaseMetaData metadata = c.getMetaData();
        ResultSet rs = metadata.getColumns("customers.*", "id.*");

        // then
        // columns will be returned: "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "COLUMN_SIZE", "NULLABLE", "REMARKS"
        assertTrue(rs.next());
        assertEquals("customers", rs.getString("TABLE_NAME"));
        assertEquals("id", rs.getString("COLUMN_NAME"));
        assertEquals("NUMBER", rs.getString("DATA_TYPE"));
        assertEquals(4, rs.getInt("COLUMN_SIZE"));
        assertEquals(false, rs.getBoolean("NULLABLE"));

        // there are two more table starting with customers and containing column starting with id
        assertTrue(rs.next());
        assertTrue(rs.next());

        // no more such columns
        assertFalse(rs.next());
    }

    @AfterClass
    public static void tearDown() {
        database.close();
    }

}
