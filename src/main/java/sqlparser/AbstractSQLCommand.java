package sqlparser;

/**
 * Created on 2014-10-04.
 */
public abstract class AbstractSQLCommand {
    public abstract Type getType();

    public static enum Type {SelectCommand, InsertCommand, DeleteCommand, Where, CreateTableCommand, DropTableCommand, }
}
