package sqlparser;

import datamodel.Identifier;

/**
 * Created on 2014-10-27.
 */
class DropTableSQLParser extends AbstractSQLParser<DropTableCommand>  {

    @Override
    DropTableCommand parse(Tokenizer tokenizer) throws SQLParseException {
        DropTableCommand ast = new DropTableCommand();
        // expect DROP TABLE
        expect(tokenizer, DROP, "Drop table statements does not contain DROP keyword");
        expect(tokenizer, TABLE, "Drop table statements does not contain TABLE keyword");

        Identifier tableName = new Identifier(tokenizer.next());
        ast.setTableName(tableName);
        return ast;
    }
}
