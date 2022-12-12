import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _06RemoveVillain {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        int villainID = Integer.parseInt(scanner.nextLine());

        PreparedStatement selectVillainID = connection.prepareStatement
                ("SELECT id, name FROM villains WHERE id = ?");
        selectVillainID.setInt(1, villainID);
        ResultSet villainSet = selectVillainID.executeQuery();

        if(!villainSet.next()){
            System.out.println("No such villain was found");
            return;
        }

        String villainName = villainSet.getString("name");

        PreparedStatement releaseMinions = connection.prepareStatement
                ("DELETE mv FROM minions_villains AS mv WHERE villain_id = ?");
        releaseMinions.setInt(1, villainID);

        int minionsAffected = releaseMinions.executeUpdate();

        PreparedStatement deleteVillain = connection.prepareStatement
                ("DELETE v FROM villains AS v WHERE id = ?");
        deleteVillain.setInt(1, villainID);

        System.out.println(villainName + " was deleted");
        System.out.println(minionsAffected + " minions released");


    }
}
