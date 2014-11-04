package sqlparser;

import datamodel.Identifier;

/**
 * Created on 2014-10-27.
 */
public class DeleteTableCommand extends AbstractSQLCommand {
    @Override
    public Type getType() {
        return Type.DeleteCommand;
    }

    private Identifier tableName;

    void setTableName(Identifier tableName) {
        this.tableName = tableName;
    }

    public Identifier getTableName() {
        return tableName;
    }

}
