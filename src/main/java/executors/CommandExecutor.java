package executors;

import sqlparser.AbstractSQLCommand;

/**
 * Created on 2014-10-23.
 */
public interface CommandExecutor {

    /**
     *
     * @param command
     * @return
     * @throws clientapi.SQLException //TODO document in javadoc everywhere when exception is returned
     */
    public int execute(AbstractSQLCommand command); // TODO should internal module have dependency (use) top level, client facing exception? Or rather have its own ExecutorException? Will it throw any exception from its own or rather rethrow exceptions from parser and diskstorage?
}
