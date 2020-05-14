package database;

import server.ClientHandler;

import java.sql.*;
import java.util.ArrayList;

//TODO switch from a hashmap to a mysql database
public class DataBase {

    Connection connection;
    ArrayList<String> keys = new ArrayList<>();
    int keyCount = 0;

    public DataBase() {
        connection = getConnection();
    }

    private Connection getConnection() {
        Connection conn = null;

        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url    = "jdbc:mysql://localhost:3306/db_user";

            String username = "root";
            String password = "SimonDao12@";

            Class.forName(driver);

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("connected to database \n");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    /*
        checks if the username already exists if not then add the user
     */
    public void addRecord(ClientHandler client, String name, String password, String color) {

        try {
            Statement insertQuery = connection.createStatement();

            //TODO id might be kinda useless
            //create a new user
            String insertIntoUser = "INSERT INTO db_user.users (name, password, color) " +
                           "VALUES ('"+name+"','"+password+"','"+color+"');";

            insertQuery.executeUpdate(insertIntoUser);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if the username in the request matches anything in the database
     * if not then add the user to the database
     *
     * @param name
     * @return
     */
    public boolean checkForDupe(String name) {

        boolean result = true;

        try {
            Statement selectQuery = connection.createStatement();
            String query = "SELECT name FROM db_user.users WHERE name LIKE '%"+name+"%';";

            ResultSet resultSet = selectQuery.executeQuery(query);

            if (resultSet.next()) {
                if (!resultSet.getString(1).equals(name)) {
                    result = false;
                }
            } else {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void getRecords() {

        try {
            Statement selectQuery = connection.createStatement();
            String query = "SELECT * FROM db_user.users";
            ResultSet table = selectQuery.executeQuery(query);

            while (table.next()) {
                System.out.print("name "+table.getString(1));
                System.out.print("pass "+table.getString(2));
                System.out.print("color "+table.getString(3));
                System.out.print("id "+table.getString(4));
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if the account information is inside the database
     *
     * @param name
     * @param password
     * @return verified
     */
    public boolean verifyLoginData(String name, String password) {

        boolean verified = false;

        try {
            Statement query = connection.createStatement();
            ResultSet results = query.executeQuery("SELECT name, password FROM db_user.users WHERE name LIKE '%"+name+"%';");

            while (results.next()) {
                if (results.getString(2).equals(password)) {
                    verified = true;
                } else {
                    verified = false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return verified;
    }

    public Object getUserProperty(String name, String column) {
        Object prop = null;
        keys = new ArrayList<>();

        try {
            Statement selectQuery = connection.createStatement();
            String query = "SELECT "+column+" FROM db_user.users WHERE name LIKE '"+name+"%';";
            ResultSet results = selectQuery.executeQuery(query);

            if(results.next()) {
                prop = results.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prop;
    }

    public ArrayList<String> getKeys(String name) {
        keys = new ArrayList<>();
        Statement selectQuery = null;
        try {
            selectQuery = connection.createStatement();
            String query = "SELECT name FROM db_user.users WHERE name LIKE '"+name+"%';";
            ResultSet results = selectQuery.executeQuery(query);

            while(results.next()) {
                keys.add(results.getString(1));
                System.out.println(keys.size());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        keyCount = keys.size();

        return keys;
    }

    public int getCount() { return keyCount; }

    public void setAsOffline(String name) {
        try {
            Statement query = connection.createStatement();

            String sql = "DELETE FROM db_user.online WHERE name LIKE '%"+name+"%';";
            query.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //remove from online database

    }

    public void setAsOnline(String name) {
        try {
            Statement query = connection.createStatement();

            String sql = "INSERT INTO db_user.online (name) VALUES ('"+name+"');";
            query.executeUpdate(sql);

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
