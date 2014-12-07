package sqlparser;

import java.util.List;

/**
 * Parses SELECT SQL statements, according to subset of SQL 99 specification.
 *
 * The following grammar (EBNF) is supported . Terminal symbols in single quotes:
 *
   <SELECT_query> ::= 'SELECT' <SELECT_LIST> <TABLE_EXPRESSION>

   <SELECT_LIST>  ::= <asterisk> | <identifier> [ { <comma> <identifier> }... ]

   <TABLE_EXPRESSION> ::= <FROM_CLAUSE> [ <WHERE_CLAUSE> ]

   <FROM_CLAUSE> ::= 'FROM' <TABLE_REFERENCE_LIST>

   <TABLE_REFERENCE_LIST> ::= <identifier>

   <WHERE_CLAUSE> ::= 'WHERE' <SEARCH_CONDITION>

   <SEARCH_CONDITION> ::=   <PREDICATE>

   <PREDICATE> ::= <comparison predicate> | <between predicate>

   <comparison predicate> ::=   <identifier> <comp op> <value>

     <comp op>    ::=
     <equals operator>
     |     <not equals operator>
     |     <less than operator>
     |     <greater than operator>
     |     <less than or equals operator>
     |     <greater than or equals operator>



   <identifier> ::= [0-9a-zA-Z]+

   <value> ::= <number> | <quoted_string>

   <number> ::=  [0-9]+

   <quoted_string> ::= ''' .+ '''

 */
class SelectSQLParser extends AbstractSQLParser<SelectCommand> {

    /*
       <SELECT_query> ::= 'SELECT' <SELECT_LIST> <TABLE_EXPRESSION>
     */
    SelectCommand parse(Tokenizer tokenizer) throws SQLParseException {

        SelectCommand ast = new SelectCommand();

        expect(tokenizer, SELECT, "Select statement does not contain SELECT clause");

        List<String> columnList = createListFromAllTokensBefore(tokenizer, FROM);
        parseSelectList(ast, tokenizer, columnList);

        parseTableExpression(ast, tokenizer);

        return ast;
    }

    /*
    <SELECT_LIST>  ::= <asterisk> | <identifier> | <quoted_string> [ { <comma> <identifier> | <quoted_string> }... ]
     */
    private void parseSelectList(SelectCommand ast, Tokenizer tokenizer, List<String> columnList) throws SQLParseException {
        if (columnList.isEmpty()) {
            throw new SQLParseException("No column list provided");
        }
        // either asterisk ONLY or the select list
        if (tokenizer.peek().equals(ASTERISK)) {
            ast.addSelectListElement(tokenizer.next()); // TODO refactor such constructs into separate tokenizer method
        } else {
            ast.addSelectListElements(parseCommaSeparatedList(columnList));
        }
    }

    /*
    <TABLE_EXPRESSION> ::= <FROM_CLAUSE> [ <WHERE_CLAUSE> ]
    */
    private void parseTableExpression(SelectCommand ast, Tokenizer tokenizer) throws SQLParseException {
        expect(tokenizer, FROM, "SELECT statement must contain FROM clause");

        List<String> fromList = createListFromAllTokensBefore(tokenizer, WHERE);
        parseFromClause(ast, fromList);

        if (tokenizer.hasNext()) {
            parseWhereClause(tokenizer);
        }
    }

    /*
      <FROM_CLAUSE> ::= 'FROM' <TABLE_REFERENCE_LIST>

      <TABLE_REFERENCE_LIST> ::= <identifier> [ { <comma> <identifier> }... ]
    */
    private void parseFromClause(SelectCommand ast, List<String> tableList) throws SQLParseException {
        if (tableList.isEmpty()) {
            throw new SQLParseException("No table list provided");
        }
        ast.addTables(parseIdentifierList(tableList));
    }

    /*
       <WHERE_CLAUSE> ::= 'WHERE' <SEARCH_CONDITION>

       <SEARCH_CONDITION> ::=   <PREDICATE>

       WHERE already consumed
     */
    private void parseWhereClause(Tokenizer tokenizer) throws SQLParseException {
        expect(tokenizer, WHERE, "table specification may be followed only by WHERE keyword and WHERE specification");
        parsePredicate(tokenizer);
    }

    private void parsePredicate(Tokenizer tokenizer) {

    }

}
