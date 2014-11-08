package sqlparser;

import datamodel.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses CREATE TABLE SQL statements, according to subset of SQL 99 specification.
 *
 * The following grammar (EBNF) is supported . Terminal symbols in single quotes:
 *
    <CREATE TABLE statement> ::= 'CREATE TABLE' <TABLE_NAME> <TABLE_ELEMENT_LIST>

    <TABLE_NAME> ::= <identifier>

    <TABLE_ELEMENT_LIST> ::=  <left_paren> <TABLE_ELEMENT> [{<comma> <TABLE_ELEMENT>}] <right_paren>

    <TABLE_ELEMENT> ::= <COLUMN_NAME> <DATA_TYPE> [<COLUMN_CONSTRAINT>]

    <COLUMN_NAME> ::= <identifier>

    <DATA_TYPE> ::= VARCHAR <left_paren> <lenght> <right_paren>
                | NUMBER [<left_paren> <lenght>[, <lenght>]  <right_paren>] // precision and scale
                | BOOLEAN

    <COLUMN_CONSTRAINT> ::= 'NOT NULL'

    <lenght> ::= unsigned integer
    <left_paren> ::= (
    <right_paren> ::= )
    <comma> ::= ,

 */
class CreateTableStatementParser extends AbstractSQLParser<CreateTableCommand> {

    private CreateTableCommand ast;

     /*
       <CREATE TABLE statement> ::= 'CREATE TABLE' <TABLE_NAME> <TABLE_ELEMENT_LIST>

       <TABLE_NAME> ::= <identifier>
     */
    CreateTableCommand parse(Tokenizer tokenizer) throws SQLParseException {
        ast = new CreateTableCommand();

        expect(tokenizer, CREATE, "Create table statement does not contain CREATE TABLE clause");
        expect(tokenizer, TABLE, "Create table statement does not contain CREATE TABLE clause");

        Identifier tableName = new Identifier(tokenizer.next());
        ast.setTableName(tableName);

        parseTableElementList(tokenizer);

        return ast;
    }

    /*
        <TABLE_ELEMENT_LIST> ::=  <left_paren> <TABLE_ELEMENT> [{<comma> <TABLE_ELEMENT>}] <right_paren>

     */
    private void parseTableElementList(Tokenizer tokenizer) throws SQLParseException {
        expect(tokenizer, LEFT_PAREN, "Left parenthesis expected before defining table columns");
        parseTableElementTokens(tokenizer);
        expect(tokenizer, RIGHT_PAREN, "Right parenthesis expected after defining table columns");
    }

    /*
        <TABLE_ELEMENT_LIST> ::=  <left_paren> <TABLE_ELEMENT> [{<comma> <TABLE_ELEMENT>}] <right_paren>

        <TABLE_ELEMENT> ::= <COLUMN_NAME> <DATA_TYPE> [<COLUMN_CONSTRAINT>]

        <COLUMN_CONSTRAINT> ::= 'NOT NULL'

     */
    private void parseTableElementTokens(Tokenizer tokenizer) throws SQLParseException {
        if (!moreTokensAvailableOtherThanRightParenthesis(tokenizer)) {
            throw new SQLParseException("table definition must contain definition of at least one table column");
        }
        while (moreTokensAvailableOtherThanRightParenthesis(tokenizer)) {
            Identifier columnName = new Identifier(tokenizer.next());
            String dataTypeString = tokenizer.next().toUpperCase();

            List<Integer> fieldSizeSpecifiers = new ArrayList<>();
            if (tokenizer.peek().equals(LEFT_PAREN)) {
                fieldSizeSpecifiers = parseFieldSizeSpecification(tokenizer);
            }

            SQLDataType columnDataType = SQLDataTypeFactory.getInstance(dataTypeString, fieldSizeSpecifiers);

            boolean isNotNull = false;
            // now we can meet only constraints specification
            if (tokenizer.hasNext() && tokenizer.peek().equalsIgnoreCase(NOT)) {
                tokenizer.next(); // consume NOT
                expect(tokenizer, NULL, "NOT keyword in NOT NULL constraint not followed by NULL keyword");
                isNotNull = true;
            }

            ast.addColumnInfo(new Column(columnName, columnDataType, isNotNull));

            // if there are more elements it means that we have another column specification to parse
            // consume the comma
            if (moreTokensAvailableOtherThanRightParenthesis(tokenizer)) {
                expect(tokenizer, COMMA, "Column specification not separated by comma for column " + columnName);
            }
        }
    }

    private boolean moreTokensAvailableOtherThanRightParenthesis(Tokenizer tokenizer) {
        return tokenizer.hasNext()&& !tokenizer.peek().equals(RIGHT_PAREN);
    }

    private List<Integer> parseFieldSizeSpecification(Tokenizer tokenizer) {
        List<Integer> fieldSizeSpecs = new ArrayList<>();

        expect(tokenizer, LEFT_PAREN, "field size specification not started with left paren");

        // for example VARCHAR(30), NUMBER(10,2)

        List<String> fieldSizeSpecsStrings;
        try {
            fieldSizeSpecsStrings = parseCommaSeparatedList(createListFromAllTokensBefore(tokenizer, RIGHT_PAREN));
        } catch (IllegalArgumentException e) {
            // be careful with rethrowing IllegalArgumentException
            throw new SQLParseException("Unable to parse field size specifier list",e);
        }
        for (String fieldSizeSpecString : fieldSizeSpecsStrings) {
            try {
                fieldSizeSpecs.add(Integer.parseInt(fieldSizeSpecString));
            } catch (NumberFormatException e) {
                throw new SQLParseException("Unable to parse field size specification", e);
            }
        }

        expect(tokenizer, RIGHT_PAREN, "field size specification not ended with right paren");

        return fieldSizeSpecs;
    }

}
