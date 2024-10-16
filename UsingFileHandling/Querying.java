package com.file;

import java.util.*;

public class Querying {

    public void select(Tables table, String selectColumn, String conditionColumn, String conditionValue) {
        System.out.println("Selected rows from table '" + table.getTableName() + "':");

        if (selectColumn.equals("*")) {
            table.display();
            return;
        }

        if (!table.getColumnNames().contains(selectColumn)) {
            System.out.println("Column '" + selectColumn + "' does not exist.");
            return;
        }

        for (Map.Entry<Integer, List<String>> entry : table.getMapRow().entrySet()) {
            String value = entry.getValue().get(table.getColumnNames().indexOf(conditionColumn));
            if (value.equals(conditionValue)) {
                System.out.println(entry.getValue());
            }
        }
    }

    public void updateRowByCondition(Tables table, String updateColumn, String newValue, String conditionColumn, String conditionValue) {
        for (Map.Entry<Integer, List<String>> entry : table.getMapRow().entrySet()) {
            String value = entry.getValue().get(table.getColumnNames().indexOf(conditionColumn));
            if (value.equals(conditionValue)) {
                entry.getValue().set(table.getColumnNames().indexOf(updateColumn), newValue);
                System.out.println("Row updated successfully.");
                //return;
            }
        }
        System.out.println("No matching rows found for update.");
    }

//    public void deleteRowByCondition(Tables table, String conditionColumn, String conditionValue) {
//        boolean found = false;
//
//        for (Map.Entry<Integer, List<String>> entry : table.getMapRow().entrySet()) {
//            String value = entry.getValue().get(table.getColumnNames().indexOf(conditionColumn));
//            if (value.equals(conditionValue)) {
//                table.getMapRow().remove(entry.getKey());
//                found = true;
//                System.out.println("Row deleted successfully.");
//                //break;
//            }
//        }
//        if (!found) {
//            System.out.println("No matching rows found for deletion.");
//        }
//    }
	public void deleteRowByCondition(Tables table, String conditionColumn, String conditionValue) {
	    List<Integer> keysToRemove = new ArrayList<>();

	    for (Map.Entry<Integer, List<String>> entry : table.getMapRow().entrySet()) {
	        String value = entry.getValue().get(table.getColumnNames().indexOf(conditionColumn));
	        if (value.equals(conditionValue)) {
	            keysToRemove.add(entry.getKey());
	        }
	    }

	    for (Integer key : keysToRemove) {
	        table.getMapRow().remove(key);
	        System.out.println("Row with key " + key + " deleted successfully.");
	    }

	    if (keysToRemove.isEmpty()) {
	        System.out.println("No matching rows found for deletion.");
	    }
	}


    public void truncate(Tables table) {
        table.getMapRow().clear();
        System.out.println("Table '" + table.getTableName() + "' truncated successfully.");
    }
}
