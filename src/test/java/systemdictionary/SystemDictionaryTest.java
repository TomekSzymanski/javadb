package systemdictionary;

import datamodel.Identifier;
import datamodel.Table;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created on 2014-11-08.
 */
public class SystemDictionaryTest {

    private static SystemDictionary dictionary = SystemDictionary.getInstance();

    @Test
    public void getTables() {
        // when there are two tables in the dictionary
        dictionary.registerTable(new Table(new Identifier("tableAAA")));
        dictionary.registerTable(new Table(new Identifier("tableBBB")));

        // when we select tables by regex:
        String regex = "tableA.*";
        List<Table> tablesView = dictionary.getTables(regex);

        // then, there is only one and correct one selected
        assertEquals(1, tablesView.size());
        assertEquals("tableAAA", tablesView.get(0).getTableName().getValue());
    }
}
