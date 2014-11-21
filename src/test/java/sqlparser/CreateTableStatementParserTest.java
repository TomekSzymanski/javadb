package sqlparser;

import datamodel.Column;
import datamodel.SQLBooleanDataType;
import datamodel.SQLNumberDataType;
import datamodel.SQLVarcharDataType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2014-10-21.
 */
public class CreateTableStatementParserTest {

    @Test
    public void oneColumnTable() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(", "personName", "VARCHAR", "(", "100", ")", ")"});
        CreateTableCommand ast = parser.parse(tokens);

        assertEquals("person", ast.getTableInfo().getTableName().getValue());

        List<Column> columnList = ast.getColumnsAsList();
        assertEquals(1, columnList.size());
        assertEquals("personName", columnList.get(0).columnName.getValue());
        assertEquals(SQLVarcharDataType.class, columnList.get(0).dataType.getClass());
        assertEquals(100, columnList.get(0).dataType.getFieldSizeSpecifier(0));
    }

    @Test (expected = IllegalArgumentException.class)
    public void varcharMissesSizeSpecification() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(", "personName", "varchar", ")"});
        parser.parse(tokens);
    }


    @Test (expected = SQLParseException.class)
    public void varcharBadSizeSpecification() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(", "personName", "VARCHAR",  "(", "NaN", ")", ")"});
        parser.parse(tokens);
    }


    @Test (expected = SQLParseException.class)
    public void missingRightParen() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(", "personName", "VARCHAR",  "(", "100", ")"});
        parser.parse(tokens);
    }

    @Test (expected = SQLParseException.class)
    public void badParen() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(", "age", "NUMBER", "("});
        parser.parse(tokens);
    }

    @Test (expected = SQLParseException.class)
    public void missingColumnSpecification() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person"});
        parser.parse(tokens);
    }

    @Test
    public void threeColumnTable() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(",
                    "personName", "VARCHAR", "(", "100", ")", ",",
                    "age", "NUMBER", ",",
                    "salary", "NUMBER",
                ")"});
        CreateTableCommand ast = parser.parse(tokens);

        assertEquals("person", ast.getTableInfo().getTableName().getValue());

        List<Column> columnList = ast.getColumnsAsList();
        assertEquals(3, columnList.size());

        assertEquals("personName", columnList.get(0).columnName.getValue());
        assertEquals(SQLVarcharDataType.class, columnList.get(0).dataType.getClass());
        assertEquals(100, columnList.get(0).dataType.getFieldSizeSpecifier(0));

        assertEquals("age", columnList.get(1).columnName.getValue());
        assertEquals(SQLNumberDataType.class, columnList.get(1).dataType.getClass());

        assertEquals("salary", columnList.get(2).columnName.getValue());
        assertEquals(SQLNumberDataType.class, columnList.get(2).dataType.getClass());
    }

    @Test
    public void notNullConstraint() throws SQLParseException {
        CreateTableStatementParser parser = new CreateTableStatementParser();
        Tokenizer tokens = new TokenizerMock(new String[]{"CREATE", "TABLE", "person", "(", "personName", "VARCHAR", "(", "100", ")", "NOT", "NULL", ")"});
        CreateTableCommand ast = parser.parse(tokens);

        Column column = ast.getColumnsAsList().get(0);
        assertEquals(Column.Nullable.ISNOTNULL, column.nullable);
    }

    @Test
    public void complexDefinition1() throws SQLParseException {
        SQLParser<CreateTableCommand> parser = new CreateTableStatementParser();
        String CREATE_TABLE_SQL = "CREATE TABLE TAB1 (" +
                "    id number not null,\n" + // TODO do we handle different line separators (OS specyfic). Also CRLF?
                "    name varchar(30),\n" +
                "    surname varchar(40),\n" +
                "    age number,\n" +
                "    active boolean not null\n" +
                ")";
        CreateTableCommand ast = parser.parse(CREATE_TABLE_SQL); //TODO: is there something wrong with inheritance model that we need to cast in unit tests?

        assertEquals("TAB1", ast.getTableInfo().getTableName().getValue());

        List<Column> columnList = ast.getColumnsAsList();
        assertEquals(5, columnList.size());

        assertEquals("id", columnList.get(0).columnName.getValue());
        assertEquals(SQLNumberDataType.class, columnList.get(0).dataType.getClass());
        assertTrue(columnList.get(0).nullable == Column.Nullable.ISNOTNULL);

        assertEquals("name", columnList.get(1).columnName.getValue());
        assertEquals(SQLVarcharDataType.class, columnList.get(1).dataType.getClass());
        assertEquals(30, columnList.get(1).dataType.getFieldSizeSpecifier(0));
        assertTrue(columnList.get(1).nullable == Column.Nullable.NULLABLE);

        assertEquals("active", columnList.get(4).columnName.getValue());
        assertEquals(SQLBooleanDataType.class, columnList.get(4).dataType.getClass());
        assertTrue(columnList.get(4).nullable == Column.Nullable.ISNOTNULL);
    }


}
