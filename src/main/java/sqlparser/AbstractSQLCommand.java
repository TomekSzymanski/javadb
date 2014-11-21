package sqlparser;

/**
 * Created on 2014-10-04.
 */
public abstract class AbstractSQLCommand {

    private Type type;

    AbstractSQLCommand(Type type) {
        this.type = type;
    }

    public  Type getType() {
        return type;
    }

    public static enum Type {SelectCommand, InsertCommand, DeleteCommand, Where, CreateTableCommand, DropTableCommand, }
}
