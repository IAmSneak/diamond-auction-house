package com.gmail.sneakdevs.diamondsauctionhouse.sql;

import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionItem;
import com.gmail.sneakdevs.diamondeconomy.sql.SQLiteDatabaseManager;

import java.sql.*;
import java.util.ArrayList;

public class AuctionHouseSQLiteDatabaseManager implements AuctionHouseDatabaseManager {
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(SQLiteDatabaseManager.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static ArrayList<AuctionItem> getItemList(){
        String sql = "SELECT * FROM auctionhouse";
        ArrayList<AuctionItem> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(SQLiteDatabaseManager.url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new AuctionItem(rs.getInt("id"), rs.getString("playeruuid"), rs.getString("owner"), rs.getInt("price"), rs.getInt("secondsleft"), rs.getString("tag")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int addItem(String playeruuid, String owner, int price, String tag, int secondsleft) {
        String sql = "INSERT INTO auctionhouse(playeruuid,owner,price,tag,secondsleft) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, playeruuid);
            pstmt.setString(2, owner);
            pstmt.setInt(3, price);
            pstmt.setString(4, tag);
            pstmt.setInt(5, secondsleft);
            pstmt.executeUpdate();
            return getMostRecentId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateTime(int id, int seconds) {
        String sql = "UPDATE auctionhouse SET secondsleft = ? WHERE id = ?";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seconds);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMostRecentId() {
        String sql = "SELECT id FROM auctionhouse ORDER BY id DESC";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
