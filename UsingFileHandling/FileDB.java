package com.file;

import java.io.*;
import java.util.*;

public class FileDB {
    private static final String DB_FOLDER = "database_files";

    public FileDB() {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void createTable(String tableName, List<String> columns) {
        if (getTableByName(tableName) != null) {
            System.out.println("Table with the name '" + tableName + "' already exists.");
        } else {
            Tables newTable = new Tables(tableName, columns);
            saveTableToFile(newTable);
            System.out.println("Table '" + tableName + "' created successfully.");
        }
    }

    public Tables getTableByName(String tableName) {
        File tableFile = new File(DB_FOLDER + "/" + tableName + ".txt");
        if (tableFile.exists()) {
            return loadTableFromFile(tableFile);
        }
        return null;
    }

    public void displayAllTables() {
        File folder = new File(DB_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("No tables in the database.");
        } else {
            System.out.println("Tables in the database:");
            for (File file : files) {
                System.out.println("- " + file.getName().replace(".txt", ""));
            }
        }
    }

    public void dropTable(String tableName) {
        File tableFile = new File(DB_FOLDER + "/" + tableName + ".txt");
        if (tableFile.exists()) {
            tableFile.delete();
            System.out.println("Table '" + tableName + "' dropped successfully.");
        } else {
            System.out.println("Table '" + tableName + "' does not exist.");
        }
    }

    public void saveTableToFile(Tables table) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FOLDER + "/" + table.getTableName() + ".txt"))) {
            writer.write(String.join(",", table.getColumnNames()));
            writer.newLine();
            
            for (Map.Entry<Integer, List<String>> entry : table.getMapRow().entrySet()) {
                System.out.println("Writing row: " + String.join(",", entry.getValue()));
                writer.write(String.join(",", entry.getValue()));
                writer.newLine();
            }
            writer.flush();
            System.out.println("Data written to " + table.getTableName() + ".txt successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Tables loadTableFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] columnNames = line.split(",");
                Tables table = new Tables(file.getName().replace(".txt", ""), new ArrayList<>(Arrays.asList(columnNames)));

                while ((line = reader.readLine()) != null) {
                    String[] rowValues = line.split(",");
                    table.addRow(table.getMapRow().size() + 1, new ArrayList<>(Arrays.asList(rowValues)), rowValues.length);
                }
                return table;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadTablesFromFiles() {
        File folder = new File(DB_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                loadTableFromFile(file);
            }
        }
    }

    public void saveTablesToFiles() {
        File folder = new File(DB_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                Tables table = loadTableFromFile(file);
                saveTableToFile(table);
            }
        }
    }
}
