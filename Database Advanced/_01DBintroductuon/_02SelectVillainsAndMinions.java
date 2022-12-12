import java.sql.*;
import java.util.Properties;

public class _02SelectVillainsAndMinions {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement stmt = connection.prepareStatement("SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'minion_count' FROM villains AS v\n" +
                " JOIN minions_villains AS mv ON v.id = mv.villain_id\n" +
                " GROUP BY v.name\n" +
                " HAVING minion_count > 15\n" +
                " ORDER BY minion_count DESC;");
        ResultSet resultSet = stmt.executeQuery();

        while(resultSet.next()){
            System.out.printf("%s %d\n", resultSet.getString("name"),
                    resultSet.getInt("minion_count"));
        }

        connection.close();

    }
}
