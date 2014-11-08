package sqlparser;

import datamodel.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 INSERT INTO <TABLE_NAME> <insert_columns_and_values>

 <insert_columns_and_values> ::= <column_list_specification> VALUES  <value_expression_list>

 <column_list_specification> ::= [ <left paren> <insert column list> <right paren> ]

 <value_expression_list> ::= <left paren> <value_expr> [ {<comma> <value_expr>} ] <right paren>
 */
class InsertSQLParser extends AbstractSQLParser<InsertCommand>  {

    // remove global declaration
    private InsertCommand ast;

    /*
     INSERT INTO <TABLE_NAME> <insert_columns_and_values>

     <insert_columns_and_values> ::= <column_list_specification> VALUES  <value_expression_list>
     */
    InsertCommand parse(Tokenizer tokenizer) throws SQLParseException {
        ast = new InsertCommand(); // new command must be created for every new parse INSERT requests
        // expect INSERT INTO
        expect(tokenizer, INSERT, "Insert statement does not contain INSERT INTO clause");
        expect(tokenizer, INTO, "Insert statement does not contain INSERT INTO clause");

        Identifier tableName = new Identifier(tokenizer.next());
        ast.setTableName(tableName);

        List<String> columnList = new ArrayList<>();
        while (tokenizer.hasNext() && !tokenizer.peek().equalsIgnoreCase(VALUES)) { // TODO refactor all those hasNext and then equals and similar into methods
            columnList.add(tokenizer.next());
        }
        if (!columnList.isEmpty()) {
            parseColumnListSpecification(columnList);
        }

        expect(tokenizer, VALUES, "Insert statement does not contain VALUES clause");

        parseValuesSpecification(tokenizer);

        return  ast;
    }
    /*
    <column_list_specification> ::= [ <left paren> <insert column list> <right paren> ]
     */
    private void parseColumnListSpecification(List<String> columnList) throws SQLParseException {
        if (!columnList.get(0).equals(LEFT_PAREN)) {
            throw new SQLParseException("Column list is missing left parenthesis");
        }
        columnList.remove(0);
        if (!columnList.get(columnList.size()-1).equals(RIGHT_PAREN)) {
            throw new SQLParseException("Column list is missing right parenthesis");
        }
        columnList.remove(columnList.size()-1);
        ast.setColumnList(parseIdentifierList(columnList));
    }

    /*
    <value_expression_list> ::= <left paren> <value_expr> [ {<comma> <value_expr>} ] <right paren>
     */
    private void parseValuesSpecification(Tokenizer tokenizer) throws SQLParseException { // TODO what if we have comma as float decimal separator?
        expect(tokenizer, LEFT_PAREN, "Missing left paren before values list");
        ast.setValuesList(parseCommaSeparatedList(createListFromAllTokensBefore(tokenizer, RIGHT_PAREN)));
        expect(tokenizer, RIGHT_PAREN, "Missing right paren after values list");
    }

}
