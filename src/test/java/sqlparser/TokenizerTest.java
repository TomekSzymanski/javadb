package sqlparser;

import org.junit.Test;

import java.util.NoSuchElementException;

import static junit.framework.Assert.*;

/**
 * Created on 2014-10-04.
 */
public class TokenizerTest {

    @Test
    public void testSelectWithColumns() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT columnA , columnB FROM tableX");
        assertTrue(t.hasNext());
        assertEquals("SELECT",  t.next());
    }

    @Test
    public void testPeekBeforeNext() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT columnA , columnB FROM tableX");
        assertEquals("SELECT", t.peek());
        assertEquals("SELECT", t.next());
    }

    @Test
    public void separatingTokenBetweenWhiteSpacesRecognized() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT columnA , columnB");
        verifyAllTokensReturned(t, new String[]{"SELECT", "columnA", ",", "columnB"});
    }

    @Test
    public void peekMayBeCalledAnyTimes() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT columnA , columnB FROM tableX");
        assertEquals("SELECT", t.peek());
        assertEquals("SELECT", t.peek());

        t.next();

        assertEquals("columnA", t.peek());
        assertEquals("columnA", t.peek());
    }

    @Test
    public void testPeek() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT columnA , columnB FROM tableX");
        assertEquals("SELECT",  t.next());

        // peek called any times will show the same
        assertEquals("columnA",  t.peek());
        assertEquals("columnA",  t.peek());

        // iteration order after peek is correct
        assertEquals("columnA",  t.next());
        assertEquals(",",  t.next());
    }

    @Test (expected = NoSuchElementException.class)
    public void peekCalledWhenNoMoreElements() {
        SimpleTokenizer t = new SimpleTokenizer("FROM tableX");
        assertEquals("FROM",  t.next());
        assertEquals("tableX",  t.next());
        t.peek();
    }

    @Test
    public void testCommaActsAsTokenDelimiter() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT columnA,columnB");
        verifyAllTokensReturned(t, new String[] {"SELECT", "columnA", ",", "columnB"});
    }

    @Test
    public void testParensActAsTokenDelimiter() {
        SimpleTokenizer t = new SimpleTokenizer("INSERT INTO T VALUES(20,30)");
        verifyAllTokensReturned(t, new String[] {"INSERT", "INTO", "T", "VALUES", "(", "20", ",", "30", ")"});
    }

    @Test
    public void testDotDoesNotDelimit() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT T1.col FROM T1");
        verifyAllTokensReturned(t, new String[] {"SELECT", "T1.col", "FROM", "T1"});
    }


    @Test
    public void testEqualActsAsTokenDelimiter() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT * FROM T1, T2 WHERE T1.col=T2.col");
        verifyAllTokensReturned(t, new String[] {"SELECT", "*", "FROM", "T1", ",", "T2", "WHERE", "T1.col", "=", "T2.col"});
    }

    @Test
    public void dotIsDecimalSeparator() {
        SimpleTokenizer t = new SimpleTokenizer("(3.14, 44)");
        verifyAllTokensReturned(t, new String[] {"(", "3.14", ",", "44", ")"});
    }

    @Test
    public void commaIsNotDecimalSeparator() {
        SimpleTokenizer t = new SimpleTokenizer("select 3,14 from dual");
        verifyAllTokensReturned(t, new String[] {"select", "3", ",", "14", "from", "dual"});
    }

    @Test
    public void testUnequalActsAsTokenDelimiterAndInequalRecognizedAsToken() {
        SimpleTokenizer t = new SimpleTokenizer("SELECT * FROM T1, T2 WHERE T1.col!=T2.col");
        verifyAllTokensReturned(t, new String[] {"SELECT", "*", "FROM", "T1", ",", "T2", "WHERE", "T1.col", "!=", "T2.col"});
    }

    @Test
    public void testGreaterThenIsTokenAndSeparatingToken() {
        SimpleTokenizer t = new SimpleTokenizer("colA>colB and colC > 34");
        verifyAllTokensReturned(t, new String[] {"colA", ">", "colB", "and", "colC", ">", "34"});
    }


    @Test
    // test for both >= and <= operators
    public void testGreaterThenEqualsIsTokenAndTokenDelimiter() {
        SimpleTokenizer t = new SimpleTokenizer("T1.col>=T2.col and T2.col<=34 and T2.colz >= 4");
        verifyAllTokensReturned(t, new String[] {"T1.col", ">=", "T2.col", "and",  "T2.col", "<=", "34", "and", "T2.colz", ">=", "4"});
    }


    private void verifyAllTokensReturned(SimpleTokenizer t, String[] expectedTokens) {
        for (String expected : expectedTokens) {
            assertEquals(expected, t.next());
        }
        assertFalse(t.hasNext());
    }
}
