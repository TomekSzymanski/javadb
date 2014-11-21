package executors;

import clientapi.Connection;
import clientapi.SQLException;
import storageapi.CollectionBasedInMemoryStorage;
import integrationtests.IntegrationTestsBase;
import org.junit.Test;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

import static org.junit.Assert.fail;

/**
 * Created on 2014-10-24.
 */
public class InsertExecutorTest {

    private static Storage storage = CollectionBasedInMemoryStorage.getInstance();
    private static SystemDictionary dictionary = SystemDictionary.getInstance();

    private static final String TEST_TAB_NAME = "INSERT_EXECUTOR_INVALID";

    @Test
    public void testInvalidColumnsProvided() {
        // given: a table with two columns is created
        Connection c = new Connection();
        IntegrationTestsBase.createTestTable(c, TEST_TAB_NAME,
                "CREATE TABLE " + TEST_TAB_NAME + " (a varchar(100), b number(10))");

        // inserting specifying existing columns should work
        c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a, b) VALUES ('tttt', 1330)");

        // when trying to specify non-existent column in INSERT
        try {
            c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a, x) VALUES ('tttt', 1330)");
            fail("SQLException should have been thrown");
        } catch (SQLException e) {}
    }

    @Test
    public void notNullColumnNotSpecifiedInInsert() {
        // given: a table with two columns is created
        Connection c = new Connection();
        IntegrationTestsBase.createTestTable(c, TEST_TAB_NAME,
                "CREATE TABLE " + TEST_TAB_NAME + " (a varchar(100), b number(10) NOT NULL)");

        // inserting specifying not null column should work
        c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a, b) VALUES ('tttt', 1330)");

        // when trying to insert without specifying non-null column in insert list:
        try {
            c.createStatement().execute("INSERT INTO " + TEST_TAB_NAME + " (a) VALUES ('tttt')");
            fail("SQLException should have been thrown");
        } catch (SQLException e) {}
    }
}
