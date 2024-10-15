package com.cp;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QueryProcessing queryProcessor = new QueryProcessing();
        Database database = new Database();

        while (true) {
            System.out.println("Enter a query (or type 'exit' to quit): ");
            String inputQuery = scanner.nextLine().trim().toLowerCase();

            if (inputQuery.equals("exit")) {
                System.out.println("Exit");
                scanner.close();
                break;
            }

            inputQuery = inputQuery.replace("(", " ( ").replace(")", " ) ").replace(";", "").replace(",", " ").trim();
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
                            Table table = database.getTableByName(tableName);

                            if (table != null) {
                                if (tokens[4].equals("(")) {
                                    List<String> rowData = new ArrayList<>();
                                    int j = 5;

                                    while (j < tokens.length && !tokens[j].equals(";")) {
                                        if (!tokens[j].equals(")") && !tokens[j].equals(",")) {
                                            rowData.add(tokens[j]);
                                        }
                                        j++;
                                    }

                                    if (!rowData.isEmpty()) {
                                        if (rowData.size() <= table.getColumnNames().size()) {
                                            table.addRow(table.getMapRow().size() + 1, rowData, rowData.size());
                                            System.out.println("Row added successfully.");
                                        } else {
                                            System.out.println("Invalid query. Rows do not match columns.");
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

                    case "SELECT":
                        tableName = tokens[3];
                        Table selectedTable = database.getTableByName(tableName);

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
                        tableName = tokens[1];
                        Table updateTable = database.getTableByName(tableName);

                        if (updateTable != null) {
                            String cleanedInput = inputQuery.replaceAll("[\";=]", "");
                            String[] token1 = cleanedInput.split("\\s+");

                            if (token1[2].equals("set") && token1[5].equals("where")) {
                                String updateColumn = token1[3].trim();
                                String newValue = token1[4].trim();
                                String conditionColumn = token1[6].trim();
                                String conditionValue = token1[7].trim();

                                queryProcessor.updateRowByCondition(updateTable, updateColumn, newValue, conditionColumn, conditionValue);
                            } else {
                                System.out.println("Invalid UPDATE query format.");
                            }
                        } else {
                            System.out.println("Table '" + tableName + "' does not exist.");
                        }
                        break;

                    case "DELETE":
                        tableName = tokens[2];
                        Table deleteTable = database.getTableByName(tableName);

                        if (deleteTable != null) {
                            if (tokens.length >= 6 && tokens[1].equals("from") && tokens[3].equals("where") && tokens[5].equals("=")) {
                                String conditionColumn = tokens[4];
                                String conditionValue = tokens[6].replaceAll("\"", "");

                                queryProcessor.deleteRowByCondition(deleteTable, conditionColumn, conditionValue);
                            } else {
                                System.out.println("Invalid DELETE query format.");
                            }
                        } else {
                            System.out.println("Table '" + tableName + "' does not exist.");
                        }
                        break;

                    case "TRUNCATE":
                        tableName = tokens[2];
                        Table truncateTable = database.getTableByName(tableName);

                        if (truncateTable != null) {
                            queryProcessor.truncate(truncateTable);
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

//
//import java.util.*;
//
////CREATE TABLE table_name (
////	    column1 datatype,
////	    column2 datatype,
////	    column3 datatype,
////	);
//
///*
//create table tab ( col1 , col2 , col3 ) ;
//insert into tab values ( value1 , value2 , value3 ) ;
//select * from tab ;
//select col1 from tab ;
//UPDATE tab SET col1 = " value2 " WHERE col2 = " value2 " ;
//DELETE FROM tab WHERE col1 = "value1";
//truncate table tab;
//show tables;
//drop table tab;
// */
//
//
//public class Main {
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        QueryProcessing queryProcessor = new QueryProcessing();
//        Table table = null;
//
//        while (true) {
////            System.out.println("\n--- MENU ---");
//            System.out.println("Enter a query (or type 'exit' to quit): ");
//            String inputQuery = scanner.nextLine().trim().toLowerCase();
////            inputQuery.toLowerCase();
//            inputQuery = inputQuery.replace("(", " ( ").replace(")", " ) ").replace(";", "").trim();
//            if (inputQuery.equals("exit")) {
//                System.out.println("Exit");
//                break;
//            }
//            
//            String[] tokens = inputQuery.split("\\s+");
//
//            if (tokens.length > 0) {
//                String command = tokens[0].toUpperCase();
//
//                switch (command) {
//                    case "CREATE":
//                        if (tokens[1].equals("table")) {
//                            String tableName = tokens[2];
////                            System.out.print("Enter column names (comma separated): ");
//                            //String[] columns = scanner.nextLine().split(",");
//                            List <String> columns = new ArrayList<>();
//                            int i = 3;
//                            if (tokens[i].equals("(")) {
//                                i++;
//                            }
//                            while(i < tokens.length && !tokens[i].equals(";")) {
//                            	if(!tokens[i].equals(")") && !tokens[i].equals(",")) {
//                            		columns.add(tokens[i]);
//                            	}
//                            	i++;
//                            }
//                            table = new Table(tableName, columns);
//                            if (columns.isEmpty()) {
//                                System.out.println("Invalid query. No columns provided.");
//                            } else {
//                                table = new Table(tableName, columns);
//                                System.out.println("Table '" + tableName + "' created successfully with columns " + columns);
//                            }
//                        }else {
//                        	System.out.println("Incorrect query");
//                        }
//                        break;
//
//                    case "INSERT":
//                        if (tokens.length >= 4 && tokens[1].equalsIgnoreCase("INTO")) {
//                            if (table != null) {
//                                String tableName = tokens[2];
//                                if (table.getTableName().equalsIgnoreCase(tableName)) {
//                                    if (tokens[3].equals("(")) {
//                                        List<String> rowData = new ArrayList<>();
//                                        int i = 4;
//
//                                        while (i < tokens.length && !tokens[i].equals(";")) {
//                                            if (!tokens[i].equals(")") && !tokens[i].equals(",")) {
//                                                rowData.add(tokens[i]);
//                                            }
//                                            i++;
//                                        }
//                                        
//                                        if (!rowData.isEmpty()) {
////                                            table.addRow(table.getMapRow().size() + 1, rowData, rowData.size());
////                                            System.out.println("Row added successfully.");
//                                        	if (rowData.size() <= table.getColumnNames().size()) {
//                                                table.addRow(table.getMapRow().size() + 1, rowData, rowData.size());
//                                                System.out.println("Row added successfully.");
//                                            } else {
//                                                System.out.println("Invalid query. Rows do not match columns.");
//                                            }
//                                        } else {
//                                            System.out.println("Invalid query. No row data provided.");
//                                        }
//                                    } else {
//                                        System.out.println("Invalid INSERT query format. Expected '('.");
//                                    }
//                                } else {
//                                    System.out.println("Table '" + tableName + "' does not exist.");
//                                }
//                            } else {
//                                System.out.println("Create a table first.");
//                            }
//                        } else {
//                            System.out.println("Invalid INSERT query format.");
//                        }
//                        break;
//
//
////                    case "SELECT":
//////                    	if (table != null && tokens[1].equals("*")) {
//////                            table.display();
//////                            
//////                        } else
////                        if (table != null) {
////                            if (tokens.length >= 5 && tokens[2].equals("from") ) {
////                                String selectColumn = tokens[1];
////                                String tableName = tokens[3];
////                                String conditionColumn = tokens[4];
////                                String conditionValue = tokens[6];
////                                if (table.getTableName().equals(tableName)) {
////                                    if (selectColumn.equals("*")) {
////                                        table.display(); // Display all columns
////                                    } else if (table.getColumnNames().contains(selectColumn)) {
////                                    	queryProcessor.select(table, selectColumn, conditionColumn, conditionValue);
////                                    } else {
////                                        System.out.println("Column '" + selectColumn + "' does not exist.");
////                                    }
////                                } 
//////                                else {
//////                                    System.out.println("Table '" + tableName + "' does not exist.");
//////                                }
//////                                queryProcessor.select(table, selectColumn, conditionColumn, conditionValue);
////                            } else {
////                                System.out.println("Invalid SELECT query format.");
////                            }
////                        } else {
////                            System.out.println("Create a table first.");
////                        }
////                        break;
//                    case "SELECT":
//                        if (table != null) {
//                            // Check for SELECT with a WHERE clause
//                            if (tokens.length >= 7 && tokens[2].equalsIgnoreCase("FROM") && tokens[5].equals("=")) {
//                                String selectColumn = tokens[1]; // Column to select
//                                String tableName = tokens[3]; // Table name
//                                String conditionColumn = tokens[4]; // Column for the condition
//                                String conditionValue = tokens[6].replaceAll("\"", ""); // Condition value, removing quotes
//                                
//                                if (table.getTableName().equalsIgnoreCase(tableName)) {
//                                    if (selectColumn.equals("*")) {
//                                        table.display(); // Display all columns
//                                    } else if (table.getColumnNames().contains(selectColumn) && table.getColumnNames().contains(conditionColumn)) {
//                                        // Call the select method to process the query
//                                        queryProcessor.select(table, selectColumn, conditionColumn, conditionValue);
//                                    } else {
//                                        System.out.println("One or more specified columns do not exist.");
//                                    }
//                                } else {
//                                    System.out.println("Table '" + tableName + "' does not exist.");
//                                }
//                            } else if (tokens.length >= 4 && tokens[2].equalsIgnoreCase("FROM")) {
//                                // Handle SELECT without WHERE clause
//                                String selectColumn = tokens[1];
//                                String tableName = tokens[3];
//                                if (table.getTableName().equalsIgnoreCase(tableName)) {
//                                    if (selectColumn.equals("*")) {
//                                        table.display(); // Display all columns
//                                    } else if (table.getColumnNames().contains(selectColumn)) {
//                                        // Display only the specified column
//                                        List<String> columnData = new ArrayList<>();
//                                        for (Map.Entry<Integer, List<String>> entry : table.getMapRow().entrySet()) {
//                                            columnData.add(entry.getValue().get(table.getColumnNames().indexOf(selectColumn)));
//                                        }
//                                        System.out.println("Column: " + selectColumn);
//                                        for (int i = 0; i < columnData.size(); i++) {
//                                            System.out.println("Row " + (i + 1) + ": " + columnData.get(i));
//                                        }
//                                    } else {
//                                        System.out.println("Column '" + selectColumn + "' does not exist.");
//                                    }
//                                } else {
//                                    System.out.println("Table '" + tableName + "' does not exist.");
//                                }
//                            } else {
//                                System.out.println("Invalid SELECT query format.");
//                            }
//                        } else {
//                            System.out.println("Create a table first.");
//                        }
//                        break;
//                    
//                    case "UPDATE":
//                        if (table != null) {
//                            
//                            String cleanedInput = inputQuery.replaceAll("[\";=]", ""); 
//                            String[] token1 = cleanedInput.split("\\s+");
//
//                            
//                            System.out.println("Cleaned Tokens for UPDATE: " + Arrays.toString(token1));
//
//                            
//                            if (token1[2].equals("set") && token1[5].equals("where")) {
//                                
//                                String updateColumn = token1[3].trim(); 
//                                String newValue = token1[4].trim(); 
//
//
//                                String conditionColumn = token1[6].trim(); 
//                                String conditionValue = token1[7].trim(); 
//                                
//                                
//                                if (table.getColumnNames().contains(updateColumn) && table.getColumnNames().contains(conditionColumn)) {
//                                   
//                                    queryProcessor.updateRowByCondition(table, updateColumn, newValue, conditionColumn, conditionValue);
//                                    System.out.println("Update successful.");
//                                } else {
//                                    System.out.println("One or more specified columns do not exist.");
//                                }
//                            } else {
//                            	System.out.println("Invalid UPDATE query format.");
//                                //System.out.println("Invalid UPDATE query format. Ensure it follows: UPDATE table_name SET column_name = \"value\" WHERE condition_column = \"condition_value\";");
//                            }
//                        } else {
//                            System.out.println("Create a table first.");
//                        }
//                        break;
//
//
//
//
////                    case "UPDATE":
////                        if (table != null) {
////                            String updateColumn = tokens[3].split("=")[0];
////                            String newValue = tokens[3].split("=")[1];
////                            String conditionColumn = tokens[5];
////                            String conditionValue = tokens[7];
////                            queryProcessor.updateRowByCondition(table, updateColumn, newValue, conditionColumn, conditionValue);
////                        } else {
////                            System.out.println("Create a table first.");
////                        }
////                        break;
//
////                    case "DELETE":
////                        if (table != null) {
////                            if (tokens[2].equals("WHERE") && tokens[4].equals("=")) {
////                                String conditionColumn = tokens[3];
////                                String conditionValue = tokens[5];
////                                queryProcessor.deleteRowByCondition(table, conditionColumn, conditionValue);
////                            } else {
////                                System.out.println("Invalid DELETE query format.");
////                            }
////                        } else {
////                            System.out.println("Create a table first.");
////                        }
////                        break;
//                        
//                    case "DELETE":
//                        if (table != null) {
//                            // Check for valid DELETE query format
//                            if (tokens.length >= 6 && tokens[2].equalsIgnoreCase("FROM") && tokens[4].equalsIgnoreCase("WHERE") && tokens[5].equals("=")) {
//                                String tableName = tokens[3]; // The table name
//                                String conditionColumn = tokens[4]; // Column for the condition
//                                String conditionValue = tokens[6].replaceAll("\"", ""); // Condition value without quotes
//                                
//                                if (table.getTableName().equalsIgnoreCase(tableName) && table.getColumnNames().contains(conditionColumn)) {
//                                    queryProcessor.deleteRowByCondition(table, conditionColumn, conditionValue);
//                                } else {
//                                    System.out.println("Table does not exist or specified column does not exist.");
//                                }
//                            } else {
//                                System.out.println("Invalid DELETE query format.");
//                            }
//                        } else {
//                            System.out.println("Create a table first.");
//                        }
//                        break;
//
//
//                    case "TRUNCATE":
//                        if (table != null) {
//                            queryProcessor.truncate(table);
//                        } else {
//                            System.out.println("Create a table first.");
//                        }
//                        break;
//
//                    default:
//                        System.out.println("Unknown command. Please try again.");
//                        break;
//                }
//            } else {
//                System.out.println("Please enter a valid query.");
//            }
//        }
//    }
//}
