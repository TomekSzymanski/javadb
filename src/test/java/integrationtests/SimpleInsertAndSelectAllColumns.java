package integrationtests;

import clientapi.Connection;
import clientapi.ResultSet;
import clientapi.SQLException;
import clientapi.Statement;
import org.junit.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2014-10-27.
 */
public class SimpleInsertAndSelectAllColumns {

    private final static String TEST_TABLE_NAME = SimpleInsertAndSelectAllColumns.class.getSimpleName() + "_customers";

    private static Statement statement;

    @Before
    public void setUp() throws SQLException {
        Connection connection = new Connection();
        statement = connection.createStatement();
        String CREATE_TABLE_SQL = "CREATE TABLE " + TEST_TABLE_NAME + " (" +
                "    id number not null,\n" +
                "    name varchar(30),\n" +
                "    surname varchar(40),\n" +
                "    age number,\n" +
                "    active boolean not null\n" +
                ")";
        statement.executeUpdate(CREATE_TABLE_SQL);
    }

    @Test
    public void allInsertedAreThenSelectedWithAsterisk() {
        try {
            statement.executeUpdate("insert into " + TEST_TABLE_NAME + " values ( 1, 'tomek', 'szymanski', 33, 'Y')");
            statement.executeUpdate("insert into " + TEST_TABLE_NAME + " values (2,'romek','whatever', null, 'Y')");
            statement.executeUpdate("insert into " + TEST_TABLE_NAME + " values (3,'atomek','whatever', null, 'N')");

            ResultSet rs = statement.executeQuery("SELECT * FROM " + TEST_TABLE_NAME);

            assertTrue(rs.next());
            // 1, 'tomek', 'szymanski', 33, 'Y'
            assertEquals(1, rs.getInt(0));
            assertEquals("tomek", rs.getString(1));
            assertEquals("szymanski", rs.getString(2));
            assertEquals(33, rs.getInt(3));
            assertEquals(true, rs.getBoolean(4));

            assertTrue(rs.next());
            // 2,'romek','whatever', null, 'Y'
            assertEquals(2, rs.getInt(0));
            assertEquals("romek", rs.getString(1));
            assertEquals("whatever", rs.getString(2));
            assertEquals(null, rs.getInt(3));
            assertEquals(true, rs.getBoolean(4));

            assertTrue(rs.next());
            // 3,'atomek','whatever', null, 'N'
            assertEquals(3, rs.getInt(0));
            assertEquals("atomek", rs.getString(1));
            assertEquals("whatever", rs.getString(2));
            assertEquals(null, rs.getInt(3));
            assertEquals(false, rs.getBoolean(4));

            assertFalse(rs.next());

        } catch (SQLException e) {
            Assert.fail(e.toString());
        }
    }


    @Test
    public void allInsertedAreThenSelectedWithAllColumnsSpecified() {
        try {
            statement.executeUpdate("insert into " + TEST_TABLE_NAME + " values ( 1, 'tomek', 'szymanski', 33, 'Y')");
            statement.executeUpdate("insert into " + TEST_TABLE_NAME + " values (2,'romek','whatever', null, 'Y')");
            statement.executeUpdate("insert into " + TEST_TABLE_NAME + " values (3,'atomek','whatever', null, 'N')");

            ResultSet rs = statement.executeQuery("SELECT id, name, surname, age, active FROM " + TEST_TABLE_NAME);

            assertTrue(rs.next());
            // 1, 'tomek', 'szymanski', 33, 'Y'
            assertEquals(1, rs.getInt(0));
            assertEquals(1, rs.getInt("id"));

            assertEquals("tomek", rs.getString(1));
            assertEquals("tomek", rs.getString("name"));

            assertEquals("szymanski", rs.getString(2));
            assertEquals("szymanski", rs.getString("surname"));

            assertEquals(33, rs.getInt(3));
            assertEquals(33, rs.getInt("age"));

            assertEquals(true, rs.getBoolean(4));
            assertEquals(true, rs.getBoolean("active"));


            assertTrue(rs.next());
            // 2,'romek','whatever', null, 'Y'
            assertEquals(2, rs.getInt(0));
            assertEquals(2, rs.getInt("id"));

            assertEquals("romek", rs.getString(1));
            assertEquals("romek", rs.getString("name"));

            assertEquals("whatever", rs.getString(2));
            assertEquals("whatever", rs.getString("surname"));

            assertEquals(null, rs.getInt(3));
            assertEquals(null, rs.getInt("age"));

            assertEquals(true, rs.getBoolean(4));
            assertEquals(true, rs.getBoolean("active"));

            assertTrue(rs.next());
            // skip validations for 3rd record
            assertFalse(rs.next());

        } catch (SQLException e) {
            Assert.fail(e.toString());
        }
    }


    @After
    public void tearDown() throws SQLException {
        statement.executeUpdate("DROP TABLE " + TEST_TABLE_NAME);
    }

}

