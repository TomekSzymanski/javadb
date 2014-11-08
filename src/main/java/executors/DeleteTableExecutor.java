package executors;

import sqlparser.AbstractSQLCommand;
import sqlparser.DeleteTableCommand;
import storageapi.Storage;

/**
 * Created on 2014-11-07.
 */
public class DeleteTableExecutor implements CommandExecutor {
    private Storage storage;

    public DeleteTableExecutor(Storage storage) {
        this.storage = storage;
    }

    @Override
    public int execute(AbstractSQLCommand command) {
        DeleteTableCommand deleteCommand = (DeleteTableCommand)command; // TODO should we catch possible ClassCastException and rethrow as SQLException? Rather not as it would a programmer error to pass here command other than InsertComamnd
        storage.deleteAll(deleteCommand.getTableName());
        return 0;
    }

}
