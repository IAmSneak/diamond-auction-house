package com.gmail.sneakdevs.diamondauctionhouse.sql;

import com.gmail.sneakdevs.diamondauctionhouse.auction.AuctionItem;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class AuctionHouseSQLiteDatabaseManager implements AuctionHouseDatabaseManager {
    public static String url;

    public static void createNewDatabase(File file) {
        url = "jdbc:sqlite:" + file.getPath().replace('\\', '/');

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        createNewTable();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS auctionhouse (id integer PRIMARY KEY AUTOINCREMENT, playeruuid text, owner text NOT NULL, price integer, tag text, secondsleft integer);";
        String sql1 = "CREATE TABLE IF NOT EXISTS expireditems (id integer PRIMARY KEY AUTOINCREMENT, playeruuid text, owner text NOT NULL, price integer, tag text);";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sql1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<AuctionItem> getItemList(){
        String sql = "SELECT * FROM auctionhouse";
        ArrayList<AuctionItem> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new AuctionItem(rs.getInt("id"), rs.getString("playeruuid"), rs.getString("owner"), rs.getInt("price"), rs.getInt("secondsleft"), rs.getString("tag")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int addItem(String playerUuid, String owner, int price, int secondsLeft, String tag) {
        String sql = "INSERT INTO auctionhouse(playeruuid,owner,price,tag,secondsleft) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, playerUuid);
            pstmt.setString(2, owner);
            pstmt.setInt(3, price);
            pstmt.setString(4, tag);
            pstmt.setInt(5, secondsLeft);
            pstmt.executeUpdate();
            return getMostRecentId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getMostRecentId() {
        String sql = "SELECT id FROM auctionhouse ORDER BY id DESC";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
