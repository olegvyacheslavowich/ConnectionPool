import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 20_ok on 11.03.2017.
 */
public class Main {

    public static void main(String[] args) throws SQLException, InterruptedException {

        final ConnectionPool pool = ConnectionPool.getInstance("jdbc:derby:DerbyDB;create=false");
        final Connection connection = pool.getConnection();

        //setTable(connection);
        //  setData(connection);

        getData(connection);
        pool.returnToPull(connection);
       // pool.closeAll();

    }

    public static void setTable(Connection connection) throws SQLException {

        String query = "CREATE TABLE Persons (\n" +
                "    PersonID int,\n" +
                "    LastName varchar(255),\n" +
                "    FirstName varchar(255),\n" +
                "    Address varchar(255),\n" +
                "    City varchar(255) \n" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } finally {
            connection.close();
        }

    }

    public static void setData(Connection connection) throws SQLException {

        String query = "INSERT INTO Persons (" +
                "PersonID, " +
                "LastName, " +
                "FirstName, " +
                "Address, " +
                "City)\n" +
                "VALUES (1,'Tom B. Erichsen','Skagen','Stavanger','Norway')";

        try (Statement statement = connection.createStatement()) {

            statement.execute(query);
        } finally {
            connection.close();
        }
    }

    public synchronized static void getData(Connection connection) throws SQLException {

        String query = "SELECT * FROM Persons";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String lastName = resultSet.getString(2);
                String firstName = resultSet.getString(3);
                String address = resultSet.getString(4);
                String city = resultSet.getString(5);

                System.out.println(id + " " + lastName + " " + firstName + " " + address + " " + city);

            }
        }
    }
}
