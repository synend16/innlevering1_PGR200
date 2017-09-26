package Innlevering1;

import java.util.ArrayList;

public class Table {
    private String primaryKey, tableName;
    private String[] dataTypes, columnNames;
    private String[][] tableFromLinesInFile;

    public Table(ArrayList<String> linesInFile, String primaryKey, String tableName, String[] dataTypes, String[] columnNames) {
        setPrimaryKey(primaryKey);
        setTableName(tableName);
        setDataTypes(dataTypes);
        setColumnNames(columnNames);
        setLinesColumnsFromFile(linesInFile);
        //if (!checkIntegrity()){throw new Exception("Integrety in file");}
    }

    public String[][] getLinesAndColumnsFromFile() {
        return tableFromLinesInFile;
    }

    public void setLinesColumnsFromFile(ArrayList<String> linesInFile) {
        this.tableFromLinesInFile = new String[linesInFile.size()][columnNames.length];
        for (int i = 0; i < linesInFile.size(); i++) {
            this.tableFromLinesInFile[i] = linesInFile.get(i).split(";");
        }
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        if (primaryKey.endsWith(";")) {
            primaryKey = primaryKey.substring(0, primaryKey.length() - 1);
        }
        this.primaryKey = primaryKey
                .toLowerCase()
                .replaceAll(";", ",");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName
                .replaceAll(";", "")
                .toLowerCase()
                .trim();
    }

    public String[] getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(String[] dataTypes) {
        this.dataTypes = dataTypes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }



    public boolean checkIntegrity(){
        if (dataTypes.length == columnNames.length && columnNames.length == getLinesAndColumnsFromFile()[0].length){
            return true;
        }
        return false;
    }

}
