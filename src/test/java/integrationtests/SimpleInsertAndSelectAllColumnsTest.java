package integrationtests;

import clientapi.*;
import org.junit.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2014-10-27.
 */
public class SimpleInsertAndSelectAllColumnsTest extends IntegrationTestsBase {

    private final static String TEST_TABLE_NAME = SimpleInsertAndSelectAllColumnsTest.class.getSimpleName() + "_customers";

    private static JavaDB database = new DatabaseBuilder().newInMemoryDatabase().build();
    private static Connection connection = database.getConnection();
    private static Statement statement;

    @Before
    public void setUp() throws SQLException {
        statement = connection.createStatement();
        String CREATE_TABLE_SQL = "CREATE TABLE " + TEST_TABLE_NAME + " (" +
                "    id number not null,\n" +
                "    name varchar(30),\n" +
                "    surname varchar(40),\n" +
                "    age number,\n" +
                "    active boolean not null\n" +
                ")";
        createTestTable(connection, TEST_TABLE_NAME, CREATE_TABLE_SQL);
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
            assertEquals(33, rs.getLong(3));
            assertEquals(true, rs.getBoolean(4));

            assertTrue(rs.next());
            // 2,'romek','whatever', null, 'Y'
            assertEquals(2, rs.getInt(0));
            assertEquals("romek", rs.getString(1));
            assertEquals("whatever", rs.getString(2));
            assertEquals(0, rs.getInt(3));
            assertEquals(true, rs.getBoolean(4));

            assertTrue(rs.next());
            // 3,'atomek','whatever', null, 'N'
            assertEquals(3, rs.getInt(0));
            assertEquals("atomek", rs.getString(1));
            assertEquals("whatever", rs.getString(2));
            assertEquals(0, rs.getInt(3));
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

            assertEquals(0, rs.getInt(3));
            assertEquals(0, rs.getInt("age"));

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
    public void dropTable() throws SQLException {
        dropTestTable(connection, TEST_TABLE_NAME);
    }

    @AfterClass
    public static void tearDown() {
        database.close();
    }

}

