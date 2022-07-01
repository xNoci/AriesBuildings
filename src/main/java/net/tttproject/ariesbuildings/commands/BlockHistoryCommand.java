package net.tttproject.ariesbuildings.commands;


import net.tttproject.ariesbuildings.AriesBuildings;
import net.tttproject.ariesbuildings.sql.ToolbarSQL;
import net.tttproject.ariesbuildings.utils.Items;
import net.tttprojekt.system.command.spigot.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockHistoryCommand extends SpigotCommand {

    public BlockHistoryCommand(JavaPlugin plugin) {
        super(plugin, "blockhistory", new String[]{"bh", "bhtool", "historytool", "ht"});
    }

    @Override
    public void onPlayerExecute(Player player, String[] args) {
        //TODO
        player.getInventory().addItem(Items.BLOCK_HISTORY.getItemStack());
    }

    @Override
    public void onConsoleExecute(CommandSender commandSender, String[] strings) {

    }
}
