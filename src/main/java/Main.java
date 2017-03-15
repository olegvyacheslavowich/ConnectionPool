import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * Created by 20_ok on 11.03.2017.
 */
public class Main {

    private static final String URL = "jdbc:derby:DerbyDB;create=false";

    public static void main(String[] args) throws SQLException, InterruptedException {


        ConnectionPool pool = ConnectionPool.getInstance(URL, 20);
        Connection connection = pool.getConnection();

        getData(connection);


        // createDB(connection);
        // setTable(connection);
        // setData(connection);
        // useBatch(connection);


    }

    public static void setTable(Connection connection) throws SQLException {

        String query = "CREATE TABLE Persons (\n" +
                "    ID int,\n" +
                "    LastName varchar(255),\n" +
                "    FirstName varchar(255),\n" +
                "    Address varchar(255),\n" +
                "    City varchar(255) \n" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    public static void setData(Connection connection) throws SQLException {

        String query = "INSERT INTO Persons (" +
                "LastName, " +
                "FirstName, " +
                "Address, " +
                "City)\n" +
                "VALUES ('Tom B. Erichsen','Skagen','Stavanger','Norway')";

        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(query);
        }
    }

    public static void getData(Connection connection) throws SQLException {

        String query = "SELECT * FROM Persons";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String lastName = resultSet.getString(2);
                String firstName = resultSet.getString(3);
                String address = resultSet.getString(4);
                String city = resultSet.getString(5);

                System.out.printf(id + " " +
                        lastName + " " +
                        firstName + " " +
                        address + " " +
                        city + "\n");
            }

        }
    }

    public static void createDB(Connection connection) throws SQLException {

        String query = "CREATE DATABASE testDB";

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);

        }
    }

    public static void useBatch(Connection connection) throws SQLException {

        String query1 = "INSERT INTO Persons (" +
                "PersonID, " +
                "LastName, " +
                "FirstName, " +
                "Address, " +
                "City)\n" +
                "VALUES (2,'Oleg','Karpenko','HO HO HO','Spain')";

        String query2 = "INSERT INTO Persons (" +
                "PersonID, " +
                "LastName, " +
                "FirstName, " +
                "Address, " +
                "City)\n" +
                "VALUES (3,'SLAVA','Karpenko','HO HO HO','Spain')";
        String query3 = "INSERT INTO Persons (" +
                "PersonID, " +
                "LastName, " +
                "FirstName, " +
                "Address, " +
                "City)\n" +
                "VALUES (4,'SASHA','Karpenko','HO HO HO','Spain')";


        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.addBatch(query1);
            statement.addBatch(query2);
            statement.addBatch(query3);
            int i[] = statement.executeBatch();
            System.out.println(Arrays.toString(i));
            connection.commit();
        }
    }
}

