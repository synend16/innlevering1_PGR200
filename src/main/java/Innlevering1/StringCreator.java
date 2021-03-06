package Innlevering1;

import java.util.*;

public class StringCreator {

    public StringCreator(){ }


    public static String getContent(TableObjectFromDB dbTable){
        StringBuilder string = new StringBuilder();
        for (String s: dbTable.getColumnName()) {
            string.append(String.format("%-30s", s));
        }
        string.append("\n");
        for (int i = 0; i < dbTable.getColumnName().length * 30; i++) { string.append("-"); }
        string.append("\n");
        for (String[] line: dbTable.getContentOfTable()) {
            for (String s: line) {
                string.append(String.format("%-30s", s));
            }
            string.append("\n");
        }
        return string.toString();
    }

    public static String getMetaData(TableObjectFromDB dbTable){
        StringBuilder string = new StringBuilder();
        string.append(String.format("%-15s%-15s%-15s\n", "Name", "Data type", "Size"));
        for (int i = 0; i < 40; i++) { string.append("-"); }
        string.append("\n");
        for (int i = 0; i < dbTable.getColumnName().length; i++) {
            string.append(String.format("%-15s%-15s%-15s\n",
                    dbTable.getColumnName()[i],
                    dbTable.getColumnTypeName()[i],
                    dbTable.getColumnDisplaySize()[i]));
        }
        return string.toString();
    }


    public static String getColumnNames(TableObjectFromDB dbTable){
        StringBuilder string = new StringBuilder();
        string.append(String.format("%-15s\n", "Column Name"));
        for (int i = 0; i < 20; i++) { string.append("-"); }
        string.append("\n");
        for (int i = 0; i < dbTable.getColumnName().length; i++) {
            string.append(String.format("%-15s\n",
                    dbTable.getColumnName()[i]));
        }
        return string.toString();
    }

}
