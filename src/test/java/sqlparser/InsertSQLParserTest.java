package sqlparser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2014-10-22.
 */
public class InsertSQLParserTest {

    @Test
    public void simpleInsertNoColumnSpecification() throws SQLParseException {
        InsertSQLParser parser = new InsertSQLParser ();
        Tokenizer tokens = new TokenizerMock(new String[]{"INSERT", "INTO", "payouts", "VALUES", "(", "100", ")"});
        InsertCommand ast = parser.parse(tokens);

        assertEquals("payouts", ast.getTableName().getValue());

        assertTrue(ast.getColumnList().isEmpty());

        assertEquals(1, ast.getValues().size());
        assertEquals("100", ast.getValues().get(0));
    }

    @Test
    public void simpleInsertWithColumnSpecification() throws SQLParseException {
        InsertSQLParser parser = new InsertSQLParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"INSERT", "INTO", "payouts", "(", "value", ")", "VALUES", "(", "100", ")"});
        InsertCommand ast = parser.parse(tokens);

        assertEquals("payouts", ast.getTableName().getValue());

        assertEquals(1 ,ast.getColumnList().size());
        assertEquals("value" ,ast.getColumnList().get(0).getValue());

        assertEquals(1, ast.getValues().size());
        assertEquals("100", ast.getValues().get(0));
    }


    @Test (expected = SQLParseException.class)
    public void missingParenAroundColumns() throws SQLParseException {
        InsertSQLParser parser = new InsertSQLParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"INSERT", "INTO", "payouts", "value", ")", "VALUES", "(", "100", ")"});
        parser.parse(tokens);
    }


    @Test (expected = SQLParseException.class)
    public void missingParenAroundValues() throws SQLParseException {
        InsertSQLParser parser = new InsertSQLParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"INSERT", "INTO", "payouts", "(", "value", ")", "VALUES", "100", ")"});
        parser.parse(tokens);
    }

}
