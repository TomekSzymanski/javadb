package sqlparser;

import datamodel.Identifier;

/**
 * Created on 2014-10-27.
 */
public class DropTableCommand extends AbstractSQLCommand {

    public DropTableCommand() {
        super(Type.DropTableCommand);
    }

    private Identifier tableName;

    void setTableName(Identifier tableName) {
        this.tableName = tableName;
    }

    public Identifier getTableName() {
        return tableName;
    }
}
