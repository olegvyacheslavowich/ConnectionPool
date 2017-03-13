import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by 20_ok on 11.03.2017.
 */
public class ConnectionPool {

    private static final int DEFAULT_CAPACITY = 10;

    private static ConnectionPool connection;
    private ArrayBlockingQueue<Connection> connections = new ArrayBlockingQueue<>(DEFAULT_CAPACITY);
    private String url;

    public ConnectionPool(String url, int capacity) {
        this.url = url;
        connections = new ArrayBlockingQueue<>(capacity);

    }

    public ConnectionPool(String url) {
        this.url = url;
    }

    /**
     * Получить свободный connection
     *
     * @return connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection connection;

        if (!connections.isEmpty()) {
            connection = connections.poll();

            if (connection.isClosed()) {
                connection = getConnection();
            }
        } else {
            connection = newConnection();
        }

        return connection;
    }

    /**
     * Конструктор с заданием размера пула
     *
     * @param url
     * @param capacity размер пула
     * @return объект ConnectionPool (вернуть существующий, либо создать новый)
     */
    public static ConnectionPool getInstance(String url, int capacity) {

        if (connection == null) {
            connection = new ConnectionPool(url, capacity);
        }

        return connection;
    }


    /**
     * Конструктор с дэфолтным значением (10)
     * Singleton
     *
     * @param url
     * @return объект ConnectionPool (вернуть существующий, либо создать новый)
     */
    public static ConnectionPool getInstance(String url) {

        if (connection == null) {
            connection = new ConnectionPool(url);
        }

        return connection;
    }

    /**
     * создать новый connection
     *
     * @return
     */
    private Connection newConnection() {

        Connection connection;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            return null;
        }

        return connection;
    }

    /**
     * вернуть connection в пул
     *
     * @param connection
     */
    public void returnToPull(Connection connection) {

        if ((connection != null) && (connections.remainingCapacity() != 0)) {
            connections.add(connection);
        }
    }


    /**
     * Закрыть все connection и почисить пул
     */
    public void closeAll() {

        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }

        connections.clear();
    }


}
