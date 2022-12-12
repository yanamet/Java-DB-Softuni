import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class _07PrintAllMinionNames {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement getMinionsCount = connection.prepareStatement
                ("SELECT name FROM minions;");
        ResultSet allMinions = getMinionsCount.executeQuery();

        List<String> minionsList = new ArrayList<>();
        while (allMinions.next()) {
            minionsList.add(allMinions.getString("name"));
        }

        boolean minionCountIsOdd = minionsList.size() % 2 != 0;

        int countIter = minionCountIsOdd
                ? minionsList.size() / 2 + minionsList.size() % 2
                : minionsList.size() / 2;

        for (int i = 0; i < countIter; i++) {
            if (i == countIter - 1 && minionCountIsOdd) {
                System.out.println(minionsList.get(i));
            } else {
                System.out.println(minionsList.get(i));
                System.out.println(minionsList.get(minionsList.size() - 1 - i));
            }
        }

        connection.close();

    }
}
