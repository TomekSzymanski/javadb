package sqlparser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created on 2014-10-04.
 */
public class SelectSQLParserTest {

    @Test
    public void simplestSelectAsterisk() throws SQLParseException {
        SelectSQLParser parser = new SelectSQLParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"SELECT", "*", "FROM", "TABLE1"});
        SelectCommand ast = parser.parse(tokens);

        assertEquals(1, ast.getSelectList().size());
        assertEquals("*", ast.getSelectList().get(0));

        assertEquals(1, ast.getTableList().size());
        assertEquals("TABLE1", ast.getTableList().get(0).getValue());
    }

    @Test
         public void simplestSelectColumnList() throws SQLParseException {
        SelectSQLParser parser = new SelectSQLParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"SELECT", "column1", ",", "column2", "FROM", "TABLE1"});
        SelectCommand ast = parser.parse(tokens);

        assertEquals(2, ast.getSelectList().size());
        assertEquals("column1", ast.getSelectList().get(0));
        assertEquals("column2", ast.getSelectList().get(1));

        assertEquals(1, ast.getTableList().size());
        assertEquals("TABLE1", ast.getTableList().get(0).getValue());
    }

    @Test (expected = SQLParseException.class)
    public void simplestSelectColumnListMissingComma() throws SQLParseException {
        SelectSQLParser parser = new SelectSQLParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"SELECT", "column1", /*missing comma*/ "column2", "FROM", "TABLE1"});
        parser.parse(tokens);
    }
}
