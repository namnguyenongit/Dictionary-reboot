package example._mydictionary;

import java.sql.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class DBController {
    private static final String dbURL = "jdbc:mysql://localhost:3306/dict";
    private static final String dbUsername = "root";
    private static final String dbPassword = "darkmoon1";
    public static ArrayList<String> words = new ArrayList<>();
    public static Deque<String> historyWords = new LinkedList<>();
    public static HashMap<String,String> dictData = new HashMap<>();

    public static void init() {
        try{
            Connection connection = DriverManager.getConnection(dbURL,dbUsername,dbPassword);
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM tbl_dict_2c");
            while(rs.next()) {
                dictData.put(rs.getString("word"), rs.getString("meaning"));
                words.add(rs.getString("word"));
            }
            rs = connection.createStatement().executeQuery("SELECT word from tbl_his");
            while(rs.next()) {
                addHistory(rs.getString("word"));
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addHistory(String input) {
        if(historyWords.contains(input)) {
            historyWords.remove(input);
            historyWords.addFirst(input);
        } else if (historyWords.size() > 8) {
            historyWords.removeLast();
            historyWords.addFirst(input);
        } else {
            historyWords.addFirst(input);
        }
    }
    public static void printHis(){
        System.out.println(historyWords);
    }

    public static void UpdateOnClose() throws SQLException {
        Connection connection = DriverManager.getConnection(dbURL,dbUsername,dbPassword);
        String insert = "INSERT INTO `tbl_his` (word) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(insert);
        for (String s : historyWords) {
            statement.setString(1, s);
            statement.execute();
        }
        statement.close();
    }

    public static void ResetOnClose() throws SQLException {
        Connection connection = DriverManager.getConnection(dbURL,dbUsername,dbPassword);
        Statement truncate = connection.createStatement();
        String resetQuery = "TRUNCATE TABLE tbl_his";
        truncate.executeUpdate(resetQuery);
        truncate.close();
    }
}
