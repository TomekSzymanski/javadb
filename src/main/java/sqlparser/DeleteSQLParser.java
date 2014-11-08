package sqlparser;

import datamodel.Identifier;

/**
 * Created on 2014-10-27.
 */
class DeleteSQLParser extends AbstractSQLParser<DeleteTableCommand> {

    @Override
    DeleteTableCommand parse(Tokenizer tokenizer) throws SQLParseException {
        DeleteTableCommand ast = new DeleteTableCommand();
        // expect DELETE FROM
        expect(tokenizer, DELETE, "Delete table statements does not contain DELETE keyword");
        expect(tokenizer, FROM, "Delete table statements does not contain FROM keyword");

        Identifier tableName = new Identifier(tokenizer.next());
        ast.setTableName(tableName);
        return ast;
    }
}
