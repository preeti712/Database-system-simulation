package com.file;

import java.util.*;
import java.io.*;
/*
create table tab ( col1 , col2 , col3 ) ;
insert into tab values ( "value1" , value2 , value3 ) ;
select * from tab ;
select col1 from tab ;
UPDATE tab SET col1 = " value2 " WHERE col2 = " value2 " ;
DELETE FROM tab WHERE col1 = "value1";
truncate table tab;
show tables;
drop table tab;
*/
public class MainMethod {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Querying queryProcessor = new Querying();
        FileDB database = new FileDB();
        database.loadTablesFromFiles();

        while (true) {
            System.out.println("Enter a query (or type 'exit' to quit): ");
            String inputQuery = scanner.nextLine().trim().toLowerCase();

            if (inputQuery.equals("exit")) {
                System.out.println("Saving and exiting...");
                database.saveTablesToFiles();
                scanner.close();
                break;
            }

            inputQuery = inputQuery.replace("(", " ( ").replace(")", " ) ").replace(";", "").replace(",", " ").replace("\"", " ").trim();
            String[] tokens = inputQuery.split("\\s+");

            if (tokens.length > 0) {
                String command = tokens[0].toUpperCase();
                String tableName = "";

                switch (command) {
                    case "CREATE":
                        if (tokens[1].equals("table")) {
                            tableName = tokens[2];
                            List<String> columns = new ArrayList<>();
                            int i = 3;
                            if (tokens[i].equals("(")) {
                                i++;
                            }
                            while (i < tokens.length && !tokens[i].equals(";")) {
                                if (!tokens[i].equals(")") && !tokens[i].equals(",")) {
                                    columns.add(tokens[i]);
                                }
                                i++;
                            }

                            if (columns.isEmpty()) {
                                System.out.println("Invalid query. No columns provided.");
                            } else {
                                database.createTable(tableName, columns);
                                System.out.println("Table '" + tableName + "' created successfully with columns " + columns);
                            }
                        } else {
                            System.out.println("Incorrect query");
                        }
                        break;

                    case "INSERT":
                        if (tokens.length >= 4 && tokens[1].equals("into")) {
                            tableName = tokens[2];
                            Tables table = database.getTableByName(tableName);

                            if (table != null) {
                                if (tokens[4].equals("(")) {
                                    List<String> rowData = new ArrayList<>();
                                    int j = 5;

                                    while (j < tokens.length && !tokens[j].equals(";")) {
                                        if (!tokens[j].equals(")") && !tokens[j].equals(",")) {
                                            rowData.add(tokens[j].trim());
                                        }
                                        j++;
                                    }

                                    if (!rowData.isEmpty()) {
                                        if (rowData.size() == table.getColumnNames().size()) {
                                            table.addRow(table.getMapRow().size() + 1, rowData, rowData.size());
                                            System.out.println("Row added successfully.");
                                            database.saveTableToFile(table);
                                        } else {
                                            System.out.println("Invalid query. Number of values does not match number of columns.");
                                        }
                                    } else {
                                        System.out.println("Invalid query. No row data provided.");
                                    }
                                } else {
                                    System.out.println("Invalid INSERT query format. Expected '('.");
                                }
                            } else {
                                System.out.println("Table '" + tableName + "' does not exist.");
                            }
                        } else {
                            System.out.println("Invalid INSERT query format.");
                        }
                        break;
                        
//                    case "INSERT":
//                        if (tokens.length >= 4 && tokens[1].equals("into")) {
//                            tableName = tokens[2];
//                            Tables table = database.getTableByName(tableName);
//
//                            if (table != null) {
//                                if (tokens[4].equals("(")) {
//                                    List<String> rowData = new ArrayList<>();
//                                    int j = 5;
//
//                                    while (j < tokens.length && !tokens[j].equals(";")) {
//                                        if (!tokens[j].equals(")") && !tokens[j].equals(",")) {
//                                            rowData.add(tokens[j]);
//                                        }
//                                        j++;
//                                    }
//
//                                    if (!rowData.isEmpty()) {
//                                        if (rowData.size() <= table.getColumnNames().size()) {
//                                            table.addRow(table.getMapRow().size() + 1, rowData, rowData.size());
//                                            System.out.println("Row added successfully.");
//                                        } else {
//                                            System.out.println("Invalid query. Rows do not match columns.");
//                                        }
//                                    } else {
//                                        System.out.println("Invalid query. No row data provided.");
//                                    }
//                                } else {
//                                    System.out.println("Invalid INSERT query format. Expected '('.");
//                                }
//                            } else {
//                                System.out.println("Table '" + tableName + "' does not exist.");
//                            }
//                        } else {
//                            System.out.println("Invalid INSERT query format.");
//                        }
//                        break;

                    case "SELECT":
                        if (tokens.length < 4) {
                            System.out.println("Invalid SELECT query format.");
                            break;
                        }
                        tableName = tokens[3];
                        Tables selectedTable = database.getTableByName(tableName);

                        if (selectedTable != null) {
                            if (tokens.length >= 7 && tokens[2].equals("from") && tokens[5].equals("=")) {
                                String selectColumn = tokens[1];
                                String conditionColumn = tokens[4];
                                String conditionValue = tokens[6].replaceAll("\"", "");

                                queryProcessor.select(selectedTable, selectColumn, conditionColumn, conditionValue);
                            } else if (tokens.length >= 4 && tokens[2].equals("from")) {
                                String selectColumn = tokens[1];

                                if (selectColumn.equals("*")) {
                                    selectedTable.display();
                                } else if (selectedTable.getColumnNames().contains(selectColumn)) {
                                    List<String> columnData = new ArrayList<>();
                                    for (Map.Entry<Integer, List<String>> entry : selectedTable.getMapRow().entrySet()) {
                                        columnData.add(entry.getValue().get(selectedTable.getColumnNames().indexOf(selectColumn)));
                                    }
                                    System.out.println("Column: " + selectColumn);
                                    for (int i = 0; i < columnData.size(); i++) {
                                        System.out.println("Row " + (i + 1) + ": " + columnData.get(i));
                                    }
                                } else {
                                    System.out.println("Column '" + selectColumn + "' does not exist.");
                                }
                            } else {
                                System.out.println("Invalid SELECT query format.");
                            }
                        } else {
                            System.out.println("Table '" + tableName + "' does not exist.");
                        }
                        break;

                    case "UPDATE":
                        if (tokens.length < 8) {
                            System.out.println("Invalid UPDATE query format.");
                            break;
                        }
                        tableName = tokens[1];
                        Tables updateTable = database.getTableByName(tableName);

                        if (updateTable != null) {
                            String cleanedInput = inputQuery.replaceAll("[\";=]", "");
                            String[] token1 = cleanedInput.split("\\s+");

                            if (token1[2].equals("set") && token1[5].equals("where")) {
                                String updateColumn = token1[3].trim();
                                String newValue = token1[4].trim();
                                String conditionColumn = token1[6].trim();
                                String conditionValue = token1[7].trim();

                                queryProcessor.updateRowByCondition(updateTable, updateColumn, newValue, conditionColumn, conditionValue);
                                database.saveTableToFile(updateTable);
                            } else {
                                System.out.println("Invalid UPDATE query format.");
                            }
                        } else {
                            System.out.println("Table '" + tableName + "' does not exist.");
                        }
                        break;

                    case "DELETE":
                        if (tokens.length < 7) {
                            System.out.println("Invalid DELETE query format.");
                            break;
                        }
                        tableName = tokens[2];
                        Tables deleteTable = database.getTableByName(tableName);

                        if (deleteTable != null) {
                            if (tokens.length >= 6 && tokens[1].equals("from") && tokens[3].equals("where") && tokens[5].equals("=")) {
                                String conditionColumn = tokens[4];
                                String conditionValue = tokens[6].replaceAll("\"", "");

                                queryProcessor.deleteRowByCondition(deleteTable, conditionColumn, conditionValue);
                                database.saveTableToFile(deleteTable);
                            } else {
                                System.out.println("Invalid DELETE query format.");
                            }
                        } else {
                            System.out.println("Table '" + tableName + "' does not exist.");
                        }
                        break;

                    case "TRUNCATE":
                        tableName = tokens[2];
                        Tables truncateTable = database.getTableByName(tableName);

                        if (truncateTable != null) {
                            queryProcessor.truncate(truncateTable);
                            database.saveTableToFile(truncateTable);
                        } else {
                            System.out.println("Table '" + tableName + "' does not exist.");
                        }
                        break;

                    case "SHOW":
                        if (tokens[1].equals("tables")) {
                            database.displayAllTables();
                        } else {
                            System.out.println("Unknown command. Please try again.");
                        }
                        break;

                    case "DROP":
                        if (tokens[1].equals("table")) {
                            tableName = tokens[2];
                            database.dropTable(tableName);
                        } else {
                            System.out.println("Invalid DROP query format.");
                        }
                        break;

                    default:
                        System.out.println("Unknown command. Please try again.");
                        break;
                }
            } else {
                System.out.println("Please enter a valid query.");
            }
        }
    }
}
