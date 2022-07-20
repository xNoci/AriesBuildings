package net.tttproject.ariesbuildings.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void handlePlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        event.setCancelled(true);


        if (event.getCause() == EntityDamageEvent.DamageCause.VOID && player.getLocation().getY() < 0) {
            Location location = player.getLocation().clone();
            location.setY(100);
            player.teleport(location);
            player.sendMessage("§cDu wurdest auf höhe 100 teleportiert, da du im Void damage bekommen hast.");
            player.sendMessage("§cSolltest du in einem Block stecken benutzt /gm 3 oder /spawn.");
            return;
        }
    }

}
