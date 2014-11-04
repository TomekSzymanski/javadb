package jdbcandoracletests;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

/**
 * Created on 2014-10-25.
 */
public class SimpleCreateInsertSelectFlowTest {

    private static Statement stmt;
    private static DatabaseMetaData metaData;

    @BeforeClass
    public static void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","TOMEK","TOMEK");
        metaData = connection.getMetaData();
        stmt = connection.createStatement();

    }

    @Test
    public void testCreateInsertSelect() throws SQLException {
        boolean isResultSet = stmt.execute("SELECT * FROM customers");
        Assert.assertTrue(isResultSet);

        isResultSet = stmt.execute("insert into customers values (1,'atomek','whatever', null, 'N')");
        Assert.assertFalse(isResultSet);

        ResultSet rs = stmt.executeQuery("insert into customers values (100,'atomek','whatever', null, 'N')");

    }

}
