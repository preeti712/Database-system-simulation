package com.file;

import java.io.*;
import java.util.*;

public class Tables implements Serializable {
    private String tableName;
    private List<String> columnNames;
    private Map<Integer, List<String>> mapRow;

    public Tables(String tableName, List<String> columnNames) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.mapRow = new HashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Map<Integer, List<String>> getMapRow() {
        return mapRow;
    }

    public void addRow(int rowNumber, List<String> rowData, int columnCount) {
        mapRow.put(rowNumber, rowData);
    }

    public void display() {
        System.out.println("Table: " + tableName);
        System.out.println(String.join(", ", columnNames));
        for (Map.Entry<Integer, List<String>> entry : mapRow.entrySet()) {
            System.out.println(entry.getKey() + ": " + String.join(", ", entry.getValue()));
        }
    }
}
