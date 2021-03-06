package Innlevering1.Database;

import Innlevering1.TableObjectFromDB;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseReader{
    private DatabaseConnector dbConnector;

    public DatabaseReader(DatabaseConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    /**
     *
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws SQLException
     */
    public TableObjectFromDB getAllTables(TableObjectFromDB tableObjectFromDB) throws SQLException{
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.
                     prepareStatement("SELECT Table_Name as TableName  " +
                             "FROM information_schema.tables " +
                             "where table_schema='" + connection.getCatalog() + "'")) {
            ResultSet result = statement.executeQuery();
            return setContentOfTableFromDB(result, tableObjectFromDB);
        }
    }

    /**
     *
     * @param tableName
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws Exception
     */
    public TableObjectFromDB getAllFromOneTable(String tableName, TableObjectFromDB tableObjectFromDB) throws SQLException{
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + "")) {
            ResultSet result = statement.executeQuery();
            return setContentOfTableFromDB(result, tableObjectFromDB);
        }catch (Exception e){
            throw new SQLException();
        }
    }

    /**
     *
     * @param tableName
     * @param columnName
     * @param parameter
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws SQLException
     */
    public TableObjectFromDB getLinesThatHasOneParameter(String tableName, String columnName,
                                              String parameter, TableObjectFromDB tableObjectFromDB) throws SQLException{
        try (Connection connection = dbConnector.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName
                + " WHERE " + columnName + " LIKE '" + parameter + "';")){
            ResultSet result = statement.executeQuery();
            return setContentOfTableFromDB(result, tableObjectFromDB);
        }
    }


    /**
     *
     * @param tableName
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws SQLException
     */
    public TableObjectFromDB countRowsInTable(String tableName, TableObjectFromDB tableObjectFromDB) throws SQLException{
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("Select count(*) as rows from " + tableName)) {
            ResultSet result = statement.executeQuery();
            return setContentOfTableFromDB(result, tableObjectFromDB);
        }
    }

    /**
     *
     * @param tableName
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws SQLException
     * @throws NullPointerException
     */
    public TableObjectFromDB getMetaDataFromTable(String tableName, TableObjectFromDB tableObjectFromDB)
            throws SQLException, NullPointerException{
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName)) {
            ResultSet result = statement.executeQuery();
            setTableMetadata(result, tableObjectFromDB);
            return tableObjectFromDB;
        }catch (NullPointerException e){
            throw new NullPointerException("Table object was not initialised");
        }
    }


    /**
     * This is a method where the user can not control what is returned. This will return an
     * object containing data from the two tables: subject and room.
     * @param tableObjectFromDB
     * @return tableObjectFromDB
     * @throws SQLException
     */
    public TableObjectFromDB getTwoConnectedTables(TableObjectFromDB tableObjectFromDB) throws SQLException {
        try (Connection connection = dbConnector.getConnection();
        PreparedStatement statement = connection.prepareStatement(buildQueryForConnectedTables())){
            ResultSet result = statement.executeQuery();
            return setContentOfTableFromDB(result, tableObjectFromDB);
        }
        catch (SQLException se){
            throw new SQLException("Tables where not in the database");
        }
    }


    private String buildQueryForConnectedTables(){
        return  "SELECT r.id AS roomId, r.roomType, s.id AS SubjectId, s.name " +
                "FROM subjectandroom sar " +
                "LEFT JOIN room r ON (sar.roomId = r.id) " +
                "LEFT JOIN subject s ON (sar.subjectId = s.id);";
    }

    /**
     * This method uses prepared statement the way it is supposed to be used.
     * The drawback by doing this is that you can not create a dynamic method for all type of tables.
     * This is only to show that i know how to use Prepared statement.
     * @param value
     * @param tableObjectFromDB
     * @return
     * @throws SQLException
     */
    public TableObjectFromDB getIdAndCapacityFromRoomWithCapacityEqualsInputValue(String value, TableObjectFromDB tableObjectFromDB) throws SQLException{
        String sqlQuery = "SELECT id, capacity FROM room WHERE capacity = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            statement.setString(1, value);
            ResultSet result = statement.executeQuery();
            return setContentOfTableFromDB(result, tableObjectFromDB);
        }
    }

    /**
     *
     * @param result
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws NullPointerException
     * @throws SQLException
     */
    private TableObjectFromDB setContentOfTableFromDB(ResultSet result, TableObjectFromDB tableObjectFromDB)
        throws NullPointerException, SQLException{
        try {
            int columnCount = result.getMetaData().getColumnCount();
            ArrayList<String[]> content = new ArrayList<>();

            while (result.next()){
                String[] line = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    line[i] = result.getString(i + 1);
                }
                content.add(line);
            }
            tableObjectFromDB.setContentOfTable(content);
            return setTableMetadata(result, tableObjectFromDB);

        }catch (NullPointerException e){
            throw new NullPointerException();
        } catch (SQLException e){
            throw new SQLException();
        }

    }

    /**
     *
     * @param result
     * @param tableObjectFromDB
     * @return Populated table from Database
     * @throws NullPointerException
     * @throws SQLException
     */
    private TableObjectFromDB setTableMetadata(ResultSet result, TableObjectFromDB tableObjectFromDB)
            throws NullPointerException, SQLException {
        try {
            ResultSetMetaData metadata = result.getMetaData();
            int columnCount = metadata.getColumnCount();

            String[] columnNames = new String[columnCount];
            String[] columnDisplaySize = new String[columnCount];
            String[] columnTypeName = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = formatColumnName(metadata.getColumnName(i + 1));
                columnDisplaySize[i] = String.valueOf(metadata.getColumnDisplaySize(i + 1));
                columnTypeName[i] = metadata.getColumnTypeName(i + 1);
            }
            tableObjectFromDB.setColumnName(columnNames);
            tableObjectFromDB.setColumnDisplaySize(columnDisplaySize);
            tableObjectFromDB.setColumnTypeName(columnTypeName);
            return tableObjectFromDB;
        }catch (NullPointerException e){
            throw new NullPointerException();
        }catch (SQLException e){
            throw new SQLException();
        }
    }

    /**
     *
     * @param columnName
     * @return Formatted String
     */
    private String formatColumnName(String columnName){
        columnName = columnName.toLowerCase().replaceAll("_", " ");
        return columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
    }
}
