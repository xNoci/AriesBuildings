package net.tttproject.ariesbuildings.scoreboardteam;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.tttproject.ariesbuildings.packets.WrapperPlayServerScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Team {

    @Getter private final String name;
    @Getter private final List<Player> players = Lists.newArrayList();
    @Getter @Setter private String displayName = "";
    @Getter @Setter private String prefix = "";
    @Getter @Setter private String suffix = "";
    @Getter @Setter private ChatColor color = ChatColor.WHITE;
    @Getter @Setter private boolean allowFriendlyFire = true;
    @Getter @Setter private boolean canSeeFriendlyInvisibles = false;
    @Getter @Setter private NameTagVisibility nameTagVisibility = NameTagVisibility.ALWAYS;
    @Getter @Setter private CollisionRule collisionRule = CollisionRule.ALWAYS;


    public Team(String name) {
        Objects.requireNonNull(name, "Name cannot be null.");
        if (name.length() > 16) name = name.substring(0, 16);
        this.name = name;
    }

    private WrapperPlayServerScoreboardTeam getPacket(Mode mode) {
        WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();

        team.setMode(mode.getModeID());
        team.setName(name);
        team.setDisplayName(WrappedChatComponent.fromText(displayName));
        team.setPrefix(WrappedChatComponent.fromText(prefix));
        team.setSuffix(WrappedChatComponent.fromText(suffix));
        team.setColor(color);
        team.setPackOptionData(getData());
        team.setNameTagVisibility(nameTagVisibility.getName());
        team.setCollisionRule(collisionRule.getName());
        team.setPlayers(players.stream().map(Player::getName).collect(Collectors.toList()));

        return team;
    }

    public void sendPacket(Player player, Mode mode) {
        getPacket(mode).sendPacket(player);
    }

    private int getData() {
        int data = 0;
        if (allowFriendlyFire) {
            data |= 1;
        }
        if (canSeeFriendlyInvisibles) {
            data |= 2;
        }
        return data;
    }

    public enum Mode {
        TEAM_CREATED(0),
        TEAM_REMOVED(1),
        TEAM_UPDATED(2),
        PLAYERS_ADDED(3),
        PLAYERS_REMOVED(4);

        @Getter private final int modeID;

        Mode(int modeID) {
            this.modeID = modeID;
        }
    }

    public enum NameTagVisibility {
        ALWAYS("always"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
        NEVER("never");

        @Getter private final String name;

        NameTagVisibility(String name) {
            this.name = name;
        }
    }

    public enum CollisionRule {
        ALWAYS("always"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam"),
        NEVER("never");

        @Getter private final String name;

        CollisionRule(String name) {
            this.name = name;
        }
    }

}
