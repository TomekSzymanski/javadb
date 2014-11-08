package executors;

import sqlparser.AbstractSQLCommand;
import sqlparser.DeleteTableCommand;
import storageapi.Storage;

/**
 * Created on 2014-11-07.
 */
public class DeleteTableExecutor implements CommandExecutor<DeleteTableCommand> {
    private Storage storage;

    public DeleteTableExecutor(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(DeleteTableCommand command) {
        storage.deleteAll(command.getTableName());
    }

}
