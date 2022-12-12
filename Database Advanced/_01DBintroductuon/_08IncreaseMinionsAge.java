import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

public class _08IncreaseMinionsAge {

    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "Yana13042308");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);


        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");



        for (int i = 0; i < input.length; i++) {
            updateMinionNameAndAge(connection, Integer.parseInt(input[i]));
        }

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

    private static void updateMinionNameAndAge(Connection connection, int id) throws SQLException {
        PreparedStatement updateMinion = connection.prepareStatement
                ("UPDATE minions \n" +
                        " SET name = LOWER(name), age = age + 1\n" +
                        " WHERE id = ?;");
        updateMinion.setInt(1, id);
        updateMinion.executeUpdate();
    }
}
