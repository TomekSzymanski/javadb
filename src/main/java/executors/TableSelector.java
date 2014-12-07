package executors;

import clientapi.ResultSet;
import datamodel.Identifier;
import storageapi.Record;
import storageapi.Storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2014-11-02.
 */
class TableSelector implements QueryAssembly {

    private static Storage storage;
    private Iterator<Record> tableIterator;
    private List<String> columnLabels = new ArrayList<>();

    private ExecutionContext context;

    TableSelector(Storage storage, Identifier tableName, List<Identifier> columnList, ExecutionContext context) {
        this.storage = storage;
        tableIterator = storage.tableIterator(tableName);
        columnLabels = columnList.stream().map(Identifier::getValue).collect(Collectors.toList());
        this.context = context;
    }

    @Override
    public ResultSet getResultSet() {
        return new IteratorBasedResultSet(tableIterator, columnLabels, context);
    }
}
