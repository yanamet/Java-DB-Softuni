import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _04MinionInfo {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);

        String[] minionInfo = scanner.nextLine().split(" ");
        String villainName = scanner.nextLine().split(" ")[1];

        String minionName = minionInfo[1];
        int minionAge = Integer.parseInt(minionInfo[2]);
        String minionTown = minionInfo[3];

        int townID = getOrInsert(connection, minionTown,
                "SELECT id FROM towns WHERE name = ?",
                "Town %s was added to the database.\n",
                "INSERT INTO towns(name) VALUES (?)");;

        int villainID = getOrInsert(connection, villainName,
                "SELECT id FROM villains WHERE name = ?",
                "Villain %s was added to the database.\n",
                "INSERT INTO villains(name, evilness_factor) VALUES(?, \'evil\')");

        insertMinionInDB(connection, minionName, minionAge, townID);

        int minionID = getMinionId(connection, minionName);

        addMinionToVillain(connection, villainID, minionID);

        System.out.printf("Successfully added %s to be minion of %s\n", minionName, villainName);

        connection.close();


    }

    private static void addMinionToVillain(Connection connection, int villainID, int minionID) throws SQLException {
        PreparedStatement insertIntoMinVil = connection.prepareStatement
                ("INSERT INTO minions_villains(minion_id, villain_id) VALUES (?, ?)");
        insertIntoMinVil.setInt(1, minionID);
        insertIntoMinVil.setInt(2, villainID);
        insertIntoMinVil.executeUpdate();
    }

    private static int getMinionId(Connection connection, String minionName) throws SQLException {
        PreparedStatement selectMinion = connection.prepareStatement
                ("SELECT id FROM minions WHERE name = ? ORDER BY id DESC");
        selectMinion.setString(1, minionName);
        ResultSet minionSet = selectMinion.executeQuery();
        minionSet.next();
        return minionSet.getInt("id");
    }


    private static void insertMinionInDB(Connection connection, String minionName, int minionAge, int townID) throws SQLException {
        PreparedStatement insertMinion = connection.prepareStatement
                ("INSERT INTO minions(name, age, town_id) VALUES (?, ?, ?)");
        insertMinion.setString(1, minionName);
        insertMinion.setInt(2, minionAge);
        insertMinion.setInt(3, townID);
        insertMinion.executeUpdate();
    }

    private static int getOrInsert(Connection connection, String name
            ,String currQuery, String printString, String insertQuery) throws SQLException {

        PreparedStatement selectQuery = connection
                .prepareStatement(currQuery);
        selectQuery.setString(1, name);
        ResultSet nameSet = selectQuery.executeQuery();

        int id = 0;
        if(!nameSet.next()){
            System.out.printf(printString, name);

            PreparedStatement insert = connection
                    .prepareStatement(insertQuery);
            insert.setString(1, name);
            insert.executeUpdate();

            ResultSet newNameSet = selectQuery.executeQuery();
            newNameSet.next();
            id = newNameSet.getInt("id");

        }else{
            id = nameSet.getInt("id");
        }
        return id;
    }

    //методи за взимане поотделно villainID И townID

    private static int getOrInsertVillainId(Connection connection, String villainName) throws SQLException {
        PreparedStatement selectVillain = connection
                .prepareStatement("SELECT id FROM villains WHERE name = ?");
        selectVillain.setString(1, villainName);
        ResultSet villainNameSet = selectVillain.executeQuery();

        int villainID = 0;
        if(!villainNameSet.next()){
            System.out.printf("Villain %s was added to the database.\n", villainName);

            PreparedStatement insertVillain = connection
                    .prepareStatement("INSERT INTO villains(name, evilness_factor) VALUES(?, \'evil\')");
            insertVillain.setString(1, villainName);
            insertVillain.executeUpdate();

            ResultSet newVillainSet = selectVillain.executeQuery();
            newVillainSet.next();
            villainID = newVillainSet.getInt("id");

        }else{
            villainID = villainNameSet.getInt("id");
        }
        return villainID;
    }

    private static int getOrInsertTown(Connection connection, String minionTown) throws SQLException {
        PreparedStatement selectTown = connection.prepareStatement("SELECT id FROM towns WHERE name = ?;");
        selectTown.setString(1, minionTown);
        ResultSet townSet = selectTown.executeQuery();

        int townID = 0;
        if (!townSet.next()) {
            System.out.printf("Town %s was added to the database.\n", minionTown);
            PreparedStatement insertTown = connection.prepareStatement
                    ("INSERT INTO towns(name) VALUES (?)");
            insertTown.setString(1, minionTown);

            insertTown.executeUpdate();

            ResultSet newTownSet = selectTown.executeQuery();
            newTownSet.next();
            townID = newTownSet.getInt("id");


        } else {
            townID = townSet.getInt("id");
        }
        return townID;
    }


}

