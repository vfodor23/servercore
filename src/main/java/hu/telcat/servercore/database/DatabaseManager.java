package hu.telcat.servercore.database;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.entity.list.CoreInventoryList;
import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.entity.list.LocationList;
import hu.telcat.servercore.entity.npc.NPCList;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.permission.GroupList;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    public void createTables() {
        Log.info("Creating MySQL tables if they don't exist");
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement locationsStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getLocationsCreateScript());
            locationsStatement.executeUpdate();
            locationsStatement.close();
            PreparedStatement settingsStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getSettingsCreateScript());
            settingsStatement.executeUpdate();
            settingsStatement.close();
            PreparedStatement itemsStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getItemsCreateScript());
            itemsStatement.executeUpdate();
            itemsStatement.close();
            PreparedStatement inventoriesStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getInventoriesCreateScript());
            inventoriesStatement.executeUpdate();
            inventoriesStatement.close();
            PreparedStatement groupsStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getGroupsCreateScript());
            groupsStatement.executeUpdate();
            groupsStatement.close();
            PreparedStatement npcStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getNPCCreateScript());
            npcStatement.executeUpdate();
            npcStatement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            Log.error("Error during the MySQL table creation!");
            e.printStackTrace();
        }
    }

    public void loadLocations() {
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            PreparedStatement groupStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getLocationGetByGroupScript());
            executeLocationStatement(groupStatement);
            groupStatement.close();
            PreparedStatement serverStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getLocationGetByNameScript());
            executeLocationStatement(serverStatement);
            serverStatement.close();
            connection.close();
        }catch (SQLException e){
            Log.error("Error during the location loading!");
            e.printStackTrace();
        }
    }

    private void executeLocationStatement(PreparedStatement statement){
        try{
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Location loc = new Location(Bukkit.getWorld(resultSet.getString("world")),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z"));
                loc.setPitch(resultSet.getFloat("pitch"));
                loc.setYaw(resultSet.getFloat("yaw"));
                LocationList.addLocation(resultSet.getString("name"), loc);
            }
            resultSet.close();
        }catch (SQLException e){
            Log.error("Error during the location statement executing!");
            e.printStackTrace();
        }
    }

    public void loadSettings() {
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            PreparedStatement groupStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getSettingsGetByGroupScript());
            executeSettingsStatement(groupStatement);
            groupStatement.close();
            PreparedStatement serverStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getSettingsGetByNameScript());
            executeSettingsStatement(serverStatement);
            serverStatement.close();
            connection.close();
        }catch (SQLException e){
            Log.error("Error during the settings loading!");
            e.printStackTrace();
        }
    }

    private void executeSettingsStatement(PreparedStatement statement){
        try{
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                ServerConstants.loadServerSettings(resultSet);
            }
            resultSet.close();
        }catch (SQLException e){
            Log.error("Error during the settings statement executing!");
            e.printStackTrace();
        }
    }

    public void loadItems() {
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            PreparedStatement groupStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getItemsGetScript());
            executeItemStatement(groupStatement);
            groupStatement.close();
            connection.close();
        }catch (SQLException e){
            Log.error("Error during the items loading!");
            e.printStackTrace();
        }
    }

    private void executeItemStatement(PreparedStatement statement){
        try{
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                ItemList.addItem(resultSet.getString("name"), resultSet);
            }
            resultSet.close();
        }catch (SQLException e){
            Log.error("Error during the item statement executing!");
            e.printStackTrace();
        }
    }

    public void loadInventories() {
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            PreparedStatement groupStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getInventoriesGetByGroupScript());
            executeInventoriesStatement(groupStatement);
            groupStatement.close();
            PreparedStatement serverStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getInventoriesGetByNameScript());
            executeInventoriesStatement(serverStatement);
            serverStatement.close();
            PreparedStatement globalStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getInventoriesGetByGlobalScript());
            executeInventoriesStatement(globalStatement);
            globalStatement.close();
            connection.close();
        }catch (SQLException e){
            Log.error("Error during the inventories loading!");
            e.printStackTrace();
        }
    }

    private void executeInventoriesStatement(PreparedStatement statement){
        try{
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                CoreInventoryList.addInventory(resultSet.getString("name"), resultSet);
            }
            resultSet.close();
        }catch (SQLException e){
            Log.error("Error during the inventories statement executing!");
            e.printStackTrace();
        }
    }

    public void loadGroups() {
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            PreparedStatement groupStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getGroupsGetScript());
            executeGroupsStatement(groupStatement);
            groupStatement.close();
            connection.close();
        }catch (SQLException e){
            Log.error("Error during the groups loading!");
            e.printStackTrace();
        }
    }

    private void executeGroupsStatement(PreparedStatement statement){
        try{
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                GroupList.loadGroup(resultSet.getString("name"), resultSet);
            }
            resultSet.close();
        }catch (SQLException e){
            Log.error("Error during the group statement execution!");
            e.printStackTrace();
        }
    }

    public void loadNPC() {
        try{
            Connection connection = ServerCore.getInstance().getMySQL().getConnection();
            PreparedStatement groupStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getNPCGetByGroupScript());
            executeNPCStatement(groupStatement);
            groupStatement.close();
            PreparedStatement serverStatement = connection
                    .prepareStatement(ServerCore.getInstance().getMySQL().getNPCGetByNameScript());
            executeNPCStatement(serverStatement);
            serverStatement.close();
            connection.close();
        }catch (SQLException e){
            Log.error("Error during the npc loading!");
            e.printStackTrace();
        }
    }

    private void executeNPCStatement(PreparedStatement statement){
        try{
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                NPCList.addNPC(resultSet.getString("name"), resultSet);
            }
            resultSet.close();
        }catch (SQLException e){
            Log.error("Error during the npc statement execution!");
            e.printStackTrace();
        }
    }
}
