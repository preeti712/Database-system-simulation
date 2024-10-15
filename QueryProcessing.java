package com.cp;

import java.util.*;

public class QueryProcessing {

    public void select(Table table, String columnName, String conditionCol, String conditionVal) {
        List<String> columnNames = table.getColumnNames();
        Map<Integer, List<String>> rows = table.getMapRow();

        int selectIndex = columnNames.indexOf(columnName);
        int conditionIndex = columnNames.indexOf(conditionCol);

        if (selectIndex == -1 || conditionIndex == -1) {
            System.out.println("Invalid column name.");
            return;
        }

        System.out.println("Results for SELECT " + columnName + " WHERE " + conditionCol + " = " + conditionVal);
        for (Map.Entry<Integer, List<String>> entry : rows.entrySet()) {
            List<String> row = entry.getValue();
            if (row.get(conditionIndex).equals(conditionVal)) {
                System.out.println(row.get(selectIndex));
            }
        }
    }

//    public void updateRowByCondition(Table table, String updateColumn, String newValue, String conditionCol, String conditionVal) {
//        List<String> columnNames = table.getColumnNames();
//        Map<Integer, List<String>> rows = table.getMapRow();
//
//        int updateIndex = columnNames.indexOf(updateColumn);
//        int conditionIndex = columnNames.indexOf(conditionCol);
//
//        if (updateIndex == -1 || conditionIndex == -1) {
//            System.out.println("Invalid column name.");
//            return;
//        }
//
//        for (Map.Entry<Integer, List<String>> entry : rows.entrySet()) {
//            List<String> row = entry.getValue();
//            if (row.get(conditionIndex).equals(conditionVal)) {
//                row.set(updateIndex, newValue);
//                System.out.println("Row updated successfully.");
//            }
//        }
//    }

    public void updateRowByCondition(Table table, String updateColumn, String newValue, String conditionCol, String conditionVal) {
        List<String> columnNames = table.getColumnNames();
        Map<Integer, List<String>> rows = table.getMapRow();

        int updateIndex = columnNames.indexOf(updateColumn);
        int conditionIndex = columnNames.indexOf(conditionCol);

        if (updateIndex == -1 || conditionIndex == -1) {
            System.out.println("Invalid column name: " + (updateIndex == -1 ? updateColumn : conditionCol));
            return;
        }

        boolean isUpdated = false;

        for (Map.Entry<Integer, List<String>> entry : rows.entrySet()) {
            List<String> row = entry.getValue();
            
            if (row.get(conditionIndex).equals(conditionVal)) {
                row.set(updateIndex, newValue);
                isUpdated = true; 
                System.out.println("Row " + entry.getKey() + " updated successfully.");
            }
        }

        if (!isUpdated) {
            System.out.println("No rows matched the condition.");
        }
    }

    public void deleteRowByCondition(Table table, String conditionCol, String conditionVal) {
        List<String> columnNames = table.getColumnNames();
        Map<Integer, List<String>> rows = table.getMapRow();

        int conditionIndex = columnNames.indexOf(conditionCol);
        if (conditionIndex == -1) {
            System.out.println("Invalid column name.");
            return;
        }

        Iterator<Map.Entry<Integer, List<String>>> iterator = rows.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<String>> entry = iterator.next();
            List<String> row = entry.getValue();
            if (row.get(conditionIndex).equals(conditionVal)) {
                iterator.remove();
                System.out.println("Row deleted successfully.");
            }
        }
    }

    public void truncate(Table table) {
        table.truncate();
        System.out.println("Table truncated successfully.");
    }
}
