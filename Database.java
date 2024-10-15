package com.cp;
import java.util.*;

public class Database {
    private Set<Table> tables;

    public Database() {
        tables = new HashSet<>();
    }

    public void createTable(String tableName, List<String> columns) {
        if (getTableByName(tableName) != null) {
            System.out.println("Table with the name '" + tableName + "' already exists.");
        } else {
            Table newTable = new Table(tableName, columns);
            tables.add(newTable);
            System.out.println("Table '" + tableName + "' created successfully.");
        }
    }

    public Table getTableByName(String tableName) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

    public void displayAllTables() {
        if (tables.isEmpty()) {
            System.out.println("No tables in the database.");
        } else {
            System.out.println("Tables in the database:");
            for (Table table : tables) {
                System.out.println("- " + table.getTableName());
            }
        }
    }

    public void dropTable(String tableName) {
        Table tableToRemove = getTableByName(tableName);
        if (tableToRemove != null) {
            tables.remove(tableToRemove);
            System.out.println("Table '" + tableName + "' dropped successfully.");
        } else {
            System.out.println("Table '" + tableName + "' does not exist.");
        }
    }

    public boolean tableExists(String tableName) {
        return getTableByName(tableName) != null;
    }
}
//package com.cp;
//
//import java.util.*;
//public class Database {
//    private Map<String, Table> tables;
//
//    public Database() {
//        tables = new HashMap<>();
//    }
//
//    public void createTable(String tableName, List<String> columns) {
//        Table table = new Table(tableName, columns);
//        tables.put(tableName, table);
//        System.out.println("Table " + tableName + " created.");
//    }
//
//    public Table getTable(String tableName) {
//        return tables.get(tableName);
//    }
//}
