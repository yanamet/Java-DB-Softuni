import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _03SelectMinionNames {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner (System.in);
        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement villainStmt = connection.prepareStatement
                ("SELECT name FROM villains WHERE id = ?");

        villainStmt.setInt(1, villainId);

        ResultSet villainSet = villainStmt.executeQuery();

        if(!villainSet.next()){
            System.out.println("No villain with ID 10 exists in the database.");
            return;
        }

        String name = villainSet.getString("name");
        System.out.println("Villain: " + name);

        PreparedStatement minionStmt = connection.prepareStatement
                ("SELECT name, age FROM minions AS m\n" +
                        "JOIN minions_villains AS mv ON m.id = mv.minion_id\n" +
                        "WHERE mv.villain_id = ?;");

        minionStmt.setInt(1, villainId);

        ResultSet minionResSet = minionStmt.executeQuery();

        for (int i = 1; minionResSet.next(); i++) {
            System.out.printf("%d. %s %d\n", i, minionResSet.getString("name"),
                    minionResSet.getInt("age"));
        }

        connection.close();

    }
}
