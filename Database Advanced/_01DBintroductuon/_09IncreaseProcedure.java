import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _09IncreaseProcedure {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner (System.in);
        int minionId = Integer.parseInt(scanner.nextLine());

        CallableStatement updateMinions = connection.prepareCall("CALL usp_get_older(?);");
        updateMinions.setInt(1, minionId);

        updateMinions.execute();

        printMinions(connection);

        connection.close();

    }

    private static void printMinions(Connection connection) throws SQLException {
        PreparedStatement selectMinion = connection.prepareStatement
                ("SELECT name, age FROM minions;");
        ResultSet minions = selectMinion.executeQuery();

        while(minions.next()){
            System.out.printf("%s %d\n", minions.getString("name"), minions.getInt("age"));
        }
    }
}
