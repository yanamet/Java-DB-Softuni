import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class _05ChangeTownNameCasing {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner (System.in);
        String country = scanner.nextLine();

        PreparedStatement updateTownNames = connection.prepareStatement
                ("UPDATE towns SET name = UPPER(name)\n" +
                        " WHERE country = ?;");
        updateTownNames.setString(1, country);

        int updatedRows = updateTownNames.executeUpdate();

        if(updatedRows == 0){
            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.\n", updatedRows);

        printTowns(connection, country);

        connection.close();

    }

    private static void printTowns(Connection connection, String country) throws SQLException {
        PreparedStatement selectTowns = connection.prepareStatement
                ("SELECT name FROM towns WHERE country = ?;");
        selectTowns.setString(1, country);
        ResultSet townSet = selectTowns.executeQuery();

        List<String> towns = new ArrayList<>();
        while(townSet.next()){
            towns.add(townSet.getString("name"));
        }

        System.out.println(towns);
    }
}
