package systemdictionary;

import clientapi.DatabaseMetaData;
import clientapi.ResultSet;
import datamodel.Identifier;
import datamodel.Table;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2014-11-08.
 */
public class SystemDictionaryTest {

    private static SystemDictionary dictionary = SystemDictionary.createEmptyDictionary();

    @Test
    public void getTables() {
        // when there are two tables in the dictionary
        dictionary.registerTable(new Table("tableAAA"));
        dictionary.registerTable(new Table("tableBBB"));

        // when we select tables by regex:
        String regex = "tableA.*";
        DatabaseMetaData metaData = dictionary.getDatabaseMetaData();
        ResultSet tablesView = metaData.getTables(regex);

        // then, there is only one and correct one selected
        assertTrue(tablesView.next());
        assertEquals("tableAAA", tablesView.getString(0));

        // no other than previous one was selected
        assertFalse(tablesView.next());

        // tear down:
        dictionary.deregisterTable(new Identifier("tableAAA"));
        dictionary.deregisterTable(new Identifier("tableBBB"));
    }

    @Test (expected = DataDictionaryException.class)
    public void tryToRegisterTheSameTableTwice() {
        Identifier table = new Identifier("AAA");
        // given
        try {
            dictionary.registerTable(new Table(table));
        } catch (DataDictionaryException e) {
            fail(e.toString());
        }
        // when trying to register the same table again, exception should be thrown
        dictionary.registerTable(new Table(table));

        //tear down:
        dictionary.deregisterTable(table);
    }

    @Test
    public void databaseMetaDataViewUpToDataWhenDBChanges() {
        DatabaseMetaData metaData = dictionary.getDatabaseMetaData();
        String testTableName = "someTableForMetadataTest";

        // given: assert there were no such table before
        assertFalse("test precondition that table " + testTableName + " does not exist before broken", metaData.getTables(testTableName).next());

        // when: new table is added to DB
        dictionary.registerTable(new Table(testTableName));

        // then it is reflected in the metadata view:
        assertTrue(metaData.getTables(testTableName).next());

        // tear down:
        dictionary.deregisterTable(new Identifier(testTableName));
    }

}
