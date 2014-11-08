package clientapi;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2014-11-08.
 *
 * Annotation @VisibleForTesting - mark package methods use by tests only
 */
public class DatabaseMetaDataTest {

    @Test
    public void getTablesByPattern() {

        Connection c = new Connection();
        Statement stmt = c.createStatement();

        // given: create 2 tables
        stmt.execute("CREATE TABLE TABAAAA111 (id NUMBER)");
        stmt.execute("CREATE TABLE TABAAAA122 (id NUMBER)");
        stmt.execute("CREATE TABLE TABXXX (id NUMBER)");

        // when metadata selected
        DatabaseMetaData metadata = c.getMetaData();
        ResultSet rs = metadata.getTables("TABA.*");

        // then
        Assert.assertTrue(rs.next());
        Assert.assertEquals("TABAAAA111", rs.getString(0));
        Assert.assertEquals("TABAAAA111", rs.getString("TABLE_NAME"));

        Assert.assertTrue(rs.next());
        Assert.assertEquals("TABAAAA122", rs.getString(0));
        Assert.assertEquals("TABAAAA122", rs.getString("TABLE_NAME"));

        Assert.assertFalse(rs.next());
    }

}
