package storageapi;

import datamodel.Column;
import datamodel.SQLDataType;
import datamodel.SQLVarcharDataType;
import datamodel.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
* Created on 2014-11-13.
*/
public class RandomRecordGenerator {

    /**
    * Randomly generates records(of type Record).
     * Field types are chosen on random before each generation
    * @return
    **/
    public static List<Record> generate(Table table, int numberOfRecords) {
//        // choose field data types first (at random)
//        List<SQLDataType> fieldDataTypeFactories = new ArrayList<>();
//        Random rand = new Random();
//        SQLDataType dataTypeValueFactory = null;
//        for (int i = 0; i < numberOfFields; i++) {
//            switch (rand.nextInt(2)) {
//                case 0: dataTypeValueFactory = SQLNumberDataType.newInstanceFromDisk(); break;
//                case 1: dataTypeValueFactory = SQLVarcharDataType.newInstanceFromDisk(rand.nextInt(100)); break;
//            }
//            fieldDataTypeFactories.add(dataTypeValueFactory);
//        }
        List<SQLDataType> fieldDataTypeFactories = table.getColumnsAsList().stream().map(Column::getDataType).collect(Collectors.toList());

        // create actual records (at random)
        List<Record> generatedRecords = new ArrayList<>();
        Random randValue = new Random();
        for (int i = 0; i < numberOfRecords; i++) {
            Record record = new Record();
            for (SQLDataType dataTypeFactory : fieldDataTypeFactories) {
                if (dataTypeFactory.getClass() == SQLVarcharDataType.class) {
                    record.add(dataTypeFactory.valueOf(String.valueOf(randValue.nextInt((int)Math.pow(10, dataTypeFactory.getFieldSizeSpecifier(0))))));
                } else {
                    record.add(dataTypeFactory.valueOf(String.valueOf(randValue.nextInt(100000))));
                }
            }
            generatedRecords.add(record);
        }
        return generatedRecords;
    }

}
