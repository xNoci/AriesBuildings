package net.tttproject.airesbuildings.utils;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.tttproject.airesbuildings.events.PlayerAFKToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class AFKHandler implements Listener {

    private static class InstanceHolder {
        private static final AFKHandler instance = new AFKHandler();
    }

    public static AFKHandler instance() {
        return InstanceHolder.instance;
    }

    private static final long AFK_TIME = TimeUnit.MINUTES.toMillis(5);

    private final HashMap<UUID, AFKStats> playerAFKStats = Maps.newHashMap();
    private BukkitRunnable afkRunnable;

    private AFKHandler() {
    }

    public void register(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void start(JavaPlugin plugin) {
        if (afkRunnable != null) {
            stop();
        }

        afkRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    AFKStats afkStats = playerAFKStats.computeIfAbsent(player.getUniqueId(), uuid -> AFKStats.defaultStaats());
                    Location currentLocation = player.getLocation();
                    Location lastLocation = afkStats.getLastKnownLocation();
                    afkStats.setLastKnownLocation(currentLocation);

                    if (afkStats.isAfk()) return; //Player is afk, so we do not have to check
                    if (!currentLocation.equals(lastLocation))
                        return; //Player has moved -> he is not afk


                    if (afkStats.getTime() <= 0) { //Player started to be afk
                        afkStats.setLastTimeMoved(System.currentTimeMillis());
                        return;
                    }

                    if (afkStats.getTime() > AFK_TIME) {
                        afkStats.setAfk(true);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            PlayerAFKToggleEvent event = new PlayerAFKToggleEvent(player, PlayerAFKToggleEvent.AFKType.AFK);
                            Bukkit.getServer().getPluginManager().callEvent(event);
                        });
                    }
                }
            }
        };

        afkRunnable.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void stop() {
        if (afkRunnable == null) return;
        afkRunnable.cancel();
        afkRunnable = null;
    }

    public boolean isAFK(UUID uuid) {
        return playerAFKStats.getOrDefault(uuid, AFKStats.defaultStaats()).isAfk();
    }

    public void removePlayer(Player player) {
        playerAFKStats.remove(player.getUniqueId());
    }

    @EventHandler
    public void handlePlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        AFKStats afkStats = playerAFKStats.computeIfAbsent(player.getUniqueId(), uuid -> AFKStats.defaultStaats());
        afkStats.setLastTimeMoved(-1L);

        if (!afkStats.isAfk()) return;
        afkStats.setAfk(false);
        PlayerAFKToggleEvent afkEvent = new PlayerAFKToggleEvent(player, PlayerAFKToggleEvent.AFKType.NO_LONGER_AFK);
        Bukkit.getServer().getPluginManager().callEvent(afkEvent);
    }

    private static class AFKStats {

        private static AFKStats defaultStaats() {
            return new AFKStats(false, -1L, null);
        }

        @Getter @Setter private boolean afk;
        @Setter private long lastTimeMoved;
        @Getter @Setter private Location lastKnownLocation;

        public AFKStats(boolean afk, long lastTimeMoved, Location location) {
            this.afk = afk;
            this.lastTimeMoved = lastTimeMoved;
            this.lastKnownLocation = location;
        }

        public long getTime() {
            if (lastTimeMoved <= 0) return 0;
            return System.currentTimeMillis() - lastTimeMoved;
        }
    }

}
