package net.tttproject.ariesbuildings.sql;

import net.tttproject.ariesbuildings.blockhistory.BlockHistory;
import net.tttproject.ariesbuildings.blockhistory.BlockHistoryAction;
import net.tttprojekt.system.ProjectSystem;
import net.tttprojekt.system.sql.Database;
import net.tttprojekt.system.utils.ProjectFuture;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class BlockHistorySQL {

    private static final Database DATABASE = ProjectSystem.getDatabase("AriesBuildings");

    public static void create() {
        DATABASE.update("CREATE TABLE IF NOT EXISTS BlockHistory (changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, uuid VARCHAR(36), action VARCHAR(10), material VARCHAR(64), world VARCHAR(32), x INT, y INT, z INT)");
    }


    public static void insert(UUID uuid, BlockHistoryAction action, Material material, Location location) {
        DATABASE.update("INSERT INTO `BlockHistory`(`uuid`, `action`, `material`, `world`, `x`, `y`, `z`) VALUES (?, ?, ?, ?, ?, ?, ?)",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid.toString());
                    preparedStatement.setString(2, action.name());
                    preparedStatement.setString(3, material.name());
                    preparedStatement.setString(4, location.getWorld() != null ? location.getWorld().getName() : "world");
                    preparedStatement.setInt(5, location.getBlockX());
                    preparedStatement.setInt(6, location.getBlockY());
                    preparedStatement.setInt(7, location.getBlockZ());
                });
    }

    public static ProjectFuture<BlockHistory> getHistory(Location location) {
        return DATABASE.queryResult("SELECT * FROM BlockHistory WHERE world=? AND x=? AND y=? AND z=?",
                        preparedStatement -> {
                            preparedStatement.setString(1, location.getWorld() != null ? location.getWorld().getName() : "world");
                            preparedStatement.setInt(2, location.getBlockX());
                            preparedStatement.setInt(3, location.getBlockY());
                            preparedStatement.setInt(4, location.getBlockZ());
                        })
                .map(resultSet -> {
                    BlockHistory blockHistory = new BlockHistory(location);

                    try {
                        while (resultSet.next()) {
                            String rawUUID = resultSet.getString("uuid");
                            String rawAction = resultSet.getString("action");
                            String rawMaterial = resultSet.getString("material");
                            Timestamp timestamp = resultSet.getTimestamp("changed_at");

                            UUID uuid = UUID.fromString(rawUUID);
                            BlockHistoryAction action = BlockHistoryAction.byName(rawAction);
                            Material material = Material.getMaterial(rawMaterial);

                            blockHistory.addEntry(uuid, action, material, timestamp.getTime());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return blockHistory;
                });
    }

}
