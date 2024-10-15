package com.cp;

import java.util.*;

public class Table {
    private String tableName;
    private List<String> columnNames;
    private Map<Integer, List<String>> mapRow;
    private int numOfColumns;

    public Table(String tableName, List<String> columns) {
        this.tableName = tableName;
        this.columnNames = new ArrayList<>(columns);
        this.mapRow = new HashMap<>();
        numOfColumns = 0;
    }

    public void addColumn(String columnName) {
        columnNames.add(columnName);
        for (Map.Entry<Integer, List<String>> entry : mapRow.entrySet()) {
            entry.getValue().add("");
        }
    }

    public void addRow(int ind, List<String> rowData, int numOfRowData) {
//    	if (numOfRowData != numOfColumns) {
//    		System.out.println("Rows do not match columns");
//    		return;
//    	}
        while (rowData.size() < columnNames.size()) {
            rowData.add("");
        }
        mapRow.put(ind, rowData);
    }

    public void updateRow(int ind, List<String> newData) {
        if (!mapRow.containsKey(ind)) {
            System.out.println("Row " + ind + " does not exist.");
            return;
        }
        numOfColumns += 1;
        addRow(ind, newData, numOfColumns);
    }

    public void deleteRow(int ind) {
        mapRow.remove(ind);
    }

    public void truncate() {
        mapRow.clear();
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

    public void display() {
        System.out.println("Table: " + tableName);
        System.out.println("Columns: " + columnNames);
        for (Map.Entry<Integer, List<String>> entry : mapRow.entrySet()) {
            System.out.println("Row " + entry.getKey() + ": " + entry.getValue());
        }
    }
}

