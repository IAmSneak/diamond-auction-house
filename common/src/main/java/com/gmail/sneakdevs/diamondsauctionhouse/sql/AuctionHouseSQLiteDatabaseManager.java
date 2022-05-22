package com.gmail.sneakdevs.diamondsauctionhouse.sql;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
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
                list.add(new AuctionItem(rs.getInt("id"), rs.getString("playeruuid"), rs.getString("owner"), rs.getString("tag"), rs.getString("item"), rs.getInt("count"), rs.getInt("price"), rs.getInt("secondsleft")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<AuctionItem> getExpiredItemList(){
        String sql = "SELECT * FROM expireditems";
        ArrayList<AuctionItem> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(SQLiteDatabaseManager.url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new AuctionItem(rs.getInt("id"), rs.getString("playeruuid"), rs.getString("owner"), rs.getString("tag"), rs.getString("item"), rs.getInt("count"), rs.getInt("price"), 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int addItemToAuction(String playeruuid, String owner, String tag, String item, int count, int price, int secondsleft) {
        String sql = "INSERT INTO auctionhouse(playeruuid,owner,tag,item,count,price,secondsleft) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, playeruuid);
            pstmt.setString(2, owner);
            pstmt.setString(3, tag);
            pstmt.setString(4, item);
            pstmt.setInt(5, count);
            pstmt.setInt(6, price);
            pstmt.setInt(7, secondsleft);
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

    public boolean isItemForAuction(int id) {
        String sql = "SELECT Count(id) FROM auctionhouse WHERE id = " + id;
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeItemFromAuction(AuctionItem item) {
        String sql = "DELETE FROM auctionhouse WHERE id = ?";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItemFromExpired(AuctionItem item) {
        String sql = "DELETE FROM expireditems WHERE id = ?";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void expireItem(AuctionItem item) {
        removeItemFromAuction(item);
        DiamondsAuctionHouse.ah.removeItem(item);
        DiamondsAuctionHouse.ei.addItem(item);
        String sql = "INSERT INTO expireditems(id,playeruuid,owner,tag,item,count,price) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, item.getId());
            pstmt.setString(2, item.getUuid());
            pstmt.setString(3, item.getOwner());
            pstmt.setString(4, item.getTag());
            pstmt.setString(5, item.getName());
            pstmt.setInt(6, item.getItemStack().getCount());
            pstmt.setInt(7, item.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int playerItemCount(String playeruuid, String table) {
        String sql = "SELECT Count(*) FROM " + table + " WHERE playeruuid = '" + playeruuid + "'";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
