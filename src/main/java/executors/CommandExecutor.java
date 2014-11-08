package executors;

import sqlparser.AbstractSQLCommand;

/**
 * Created on 2014-10-23.
 */
public interface CommandExecutor<T extends AbstractSQLCommand> {

    /**
     *
     * @param command
     * @return
     * @throws clientapi.SQLException //TODO document in javadoc everywhere when exception is returned
     */
    public void execute(T command); // TODO should internal module have dependency (use) top level, client facing exception? Or rather have its own ExecutorException? Will it throw any exception from its own or rather rethrow exceptions from parser and diskstorage?
}
