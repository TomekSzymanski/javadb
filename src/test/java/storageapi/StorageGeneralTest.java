package storageapi;

import datamodel.*;
import org.junit.Ignore;
import org.junit.Test;
import systemdictionary.SystemDictionary;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created on 2014-11-13.
 */
public abstract class StorageGeneralTest {
    protected final Storage storage = initializeStorage(); // WARN: this, as like as constructors, will be called by jUnit for every test method!
    protected RandomRecordGenerator recordGenerator;

    private SystemDictionary systemDictionary = storage.getSystemDictionary();

    protected StorageGeneralTest(RandomRecordGenerator recordGenerator) {
        this.recordGenerator = recordGenerator;
    }

    public StorageGeneralTest() {
    }

    /**
     * Concrete subclasses extending StorageGeneralTest test must implement initializeStorage method and return instance of concrete Storage implementation"
     * WARN: this will be called per every test method
     * @return
     */
    protected abstract Storage initializeStorage();

    @Test
    public void createAndDropTable() {
        final Identifier tableName = new Identifier("createAndDropTableTest");
        storage.createTable(tableName);
        systemDictionary.registerTable(new Table(tableName));
        storage.dropTable(tableName);
    }

    @Test
    public void tryToCreateTableTwice() {
        final Identifier tableName = new Identifier("tryToCreateTableTwice");
        storage.createTable(tableName);
        systemDictionary.registerTable(new Table(tableName));
        try {
            storage.createTable(tableName);
        } catch (Exception e) {
            fail("no Exception should be thrown when trying to create existing table again");
        } finally {
            // tear down
            storage.dropTable(tableName);
        }

    }

    @Test
    public void dropNonExistingTable() {
        final Identifier tableName = new Identifier("dropNonExistingTable");
        try {
            storage.dropTable(tableName);

        } catch (Exception e) {
            fail("no exception should be thrown when trying to drop non existing table");
        }
    }

    @Test
    public void insertAndSelectSimpleVarchars() {
        // setup
        final Identifier tableName = new Identifier("insertAndSelectSimpleVarchars");
        storage.createTable(tableName);

        Table table = new Table(tableName);
        table.addColumn(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever2"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        systemDictionary.registerTable(table);

        // when
        Record recordInserted = RecordBuilder.newRecord().varchar("TOMEK").varchar("romek").build();
        storage.insertRecord(tableName, recordInserted);
        Iterator<Record> iterator = storage.tableIterator(tableName);

        // then
        assertTrue("there should be one record returned", iterator.hasNext());
        Record recordSelected = iterator.next();
        assertEquals(2, recordSelected.size());
        assertEquals("TOMEK", recordSelected.get(0).toString());
        assertEquals("romek", recordSelected.get(1).toString());

        assertFalse("no more records should be returned", iterator.hasNext());

        // tear down
        storage.dropTable(tableName);
    }

    @Test
    public void insertAndSelectSimpleVarcharsTwoRecords() {
        // setup
        final Identifier tableName = new Identifier("insertAndSelectSimpleVarcharsTwoRecords");
        storage.createTable(tableName);

        Table table = new Table(tableName);
        table.addColumn(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever2"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        systemDictionary.registerTable(table);

        // when
        Record recordInserted1 = RecordBuilder.newRecord().varchar("tomek").varchar("romek").build();
        Record recordInserted2 = RecordBuilder.newRecord().varchar("dog").varchar("cat").build();
        storage.insertRecord(tableName, recordInserted1);
        storage.insertRecord(tableName, recordInserted2);
        Iterator<Record> iterator = storage.tableIterator(tableName);

        // then
        assertTrue(iterator.hasNext());
        Record recordSelected1 = iterator.next();
        assertEquals("tomek", recordSelected1.get(0).toString());
        assertEquals("romek", recordSelected1.get(1).toString());

        assertTrue(iterator.hasNext());
        Record recordSelected2 = iterator.next();
        assertEquals("dog", recordSelected2.get(0).toString());
        assertEquals("cat", recordSelected2.get(1).toString());

        assertFalse("no more records should be returned", iterator.hasNext());

        // tear down
        storage.dropTable(tableName);
    }

    @Test @Ignore
    public void insertAndSelectFloatValue() {
        // setup
        final Identifier tableName = new Identifier("insertAndSelectFloatValue");
        storage.createTable(tableName);

        Table table = new Table(tableName);
        table.addColumn(new Column(new Identifier("floatColumn"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(20))));

    }


    @Test
    public void insertAndSelectComplexRecord() {
        // setup
        final Identifier tableName = new Identifier("insertAndSelectComplexRecord");
        storage.createTable(tableName);

        Table table = new Table(tableName);
        table.addColumn(new Column(new Identifier("whatever1"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever2"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever3"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever4"), SQLDataTypeFactory.getInstance(SQLDataType.BOOLEAN, Collections.emptyList())));
        table.addColumn(new Column(new Identifier("whatever5"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever7"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(20, 20)))); // null
        table.addColumn(new Column(new Identifier("whatever8"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("whatever9"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        // table.addColumn(new Column(new Identifier("whatever6"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(new Integer[]{20, 20})), false)); TODO handle floats
        systemDictionary.registerTable(table);


        // when
        Record recordInserted = new Record();
        recordInserted.add(new IntegerValue(1));
        recordInserted.add(new IntegerValue(200));
        recordInserted.add(new VarcharValue("tomek"));
        recordInserted.add(BooleanValue.TRUE);
        recordInserted.add(new VarcharValue("sssssssSSSS"));
        recordInserted.add(NullValue.NULL);
        recordInserted.add(new IntegerValue(23244));
        recordInserted.add(new VarcharValue("C"));
        //recordInserted.add(new FloatValue("343.232"));

        storage.insertRecord(tableName, recordInserted);
        Iterator<Record> iterator = storage.tableIterator(tableName);

        // then
        assertTrue("there should be only one record returned", iterator.hasNext());
        Record recordSelected = iterator.next();
        assertEquals(recordInserted.size(), recordSelected.size());
        assertEquals(1, recordSelected.get(0).intValue());
        assertEquals(200, recordSelected.get(1).intValue());
        assertEquals("tomek", recordSelected.get(2).toString());
        assertEquals(true, recordSelected.get(3).booleanValue());
        assertEquals("sssssssSSSS", recordSelected.get(4).toString());
        assertEquals(0, recordSelected.get(5).intValue()); // NULL value
        assertEquals(23244, recordSelected.get(6).intValue());
        assertEquals("C", recordSelected.get(7).toString());
        // assertEquals(343.232f, recordSelected.get(5).floatValue(), 0.00001f);

        assertFalse("no more records should be returned", iterator.hasNext());

        // tear down
        storage.dropTable(tableName);
    }

    @Test
    public void insertSelectDropSimpleVarchars() {
        // setup
        final Identifier tableName = new Identifier("insertSelectDropSimpleVarchars");
        storage.createTable(tableName);
        systemDictionary.registerTable(new Table(tableName));

        // when
        Record recordInserted = RecordBuilder.newRecord().varchar("tomek").varchar("romek").build();
        storage.insertRecord(tableName, recordInserted);

        // then
        storage.dropTable(tableName);
        Iterator<Record> iterator = storage.tableIterator(tableName);
        assertFalse(iterator.hasNext());
    }

    @Test
         public void insertSelectDeleteSimpleVarchars() {
        // setup
        final Identifier tableName = new Identifier("insertSelectDeleteSimpleVarchars");
        storage.createTable(tableName);
        systemDictionary.registerTable(new Table(tableName));

        // when
        Record recordInserted = RecordBuilder.newRecord().varchar("tomek").varchar("romek").build();
        storage.insertRecord(tableName, recordInserted);

        // then
        storage.deleteAll(tableName);
        Iterator<Record> iterator = storage.tableIterator(tableName);
        assertFalse(iterator.hasNext());

        // tear down
        storage.dropTable(tableName);
    }

    /**
     * WARN: this test is NOT deterministic. But we intend to, we treat it a a test mine to get variety of test cases.
     * When some instance of this test does not pass then we persist it as separate deterministic test - only for some specific instance of input data
     */
    @Test
    public void insertAndSelectBigRecordNonDeterministic() {
        // setup
        final Identifier tableName = new Identifier("insertAndSelectBigRecordNonDeterministic");
        storage.createTable(tableName);

        Table table = new Table(tableName);
        table.addColumn(new Column(new Identifier("col1"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        table.addColumn(new Column(new Identifier("col2"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(30))));
        table.addColumn(new Column(new Identifier("col3"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(10))));
        systemDictionary.registerTable(table);

        // when: insert generated records
        int numberOfRecords = 2000;
        List<Record> generatedRecords = RandomRecordGenerator.generate(table, numberOfRecords);
        generatedRecords.forEach(record -> storage.insertRecord(tableName, record));

        Iterator<Record> iterator = storage.tableIterator(tableName);

        // then
        for (int i = 0; i < generatedRecords.size(); i++) {
            assertTrue("record number " + i + " was not selected", iterator.hasNext());

            Record recordSelected = iterator.next();
            Record recordGenerated = generatedRecords.get(i);
            assertEquals(recordSelected.size(), recordGenerated.size());

            // compare every field, according to every data representation (int, string) that can be created from any other data
            for (int j = 0; j < recordGenerated.size(); j++) {
                assertEquals(recordSelected.get(j).intValue(), recordSelected.get(j).intValue());
                // TODO assertEquals(recordSelected.get(j).floatValue(), recordSelected.get(j).floatValue(), 0.00001);
                assertEquals(recordSelected.get(j).toString(), recordSelected.get(j).toString());
            }
        }

        // tear down
        storage.dropTable(tableName);
    }

}
