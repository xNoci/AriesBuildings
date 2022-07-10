package net.tttproject.ariesbuildings.blockhistory;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.tttproject.system.ProjectSystem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import xyz.upperlevel.spigot.book.BookUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BlockHistoryEntry {

    private static final SimpleDateFormat COMPACT_FORMAT = new SimpleDateFormat("dd.MM HH:mm:ss");
    private static final SimpleDateFormat DETAILED_FORMAT = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    @Getter private final UUID uuid;
    @Getter private final BlockHistoryAction action;
    @Getter private final Material material;
    @Getter private final long timestamp;

    protected BlockHistoryEntry(UUID uuid, BlockHistoryAction action, Material material, long timestamp) {
        this.uuid = uuid;
        this.action = action;
        this.material = material;
        this.timestamp = timestamp;
    }

    protected BaseComponent getBookText() {
        String name = ProjectSystem.getUserAPI().getName(uuid).getUninterruptibly();
        if (StringUtils.isBlank(name)) {
            name = "Unknown";
        }

        String actionInfo = StringUtils.isBlank(action.getDetailedInfo()) ? "" : "§bAction Details§7:\n§b" + action.getDetailedInfo() + "\n";

        return BookUtil.TextBuilder
                .of("§7[§c%s§7] §0%s by %s".formatted(COMPACT_FORMAT.format(new Date(timestamp)), action.getActionName(), name))
                .onHover(BookUtil.HoverAction.showText(
                        """
                                §bUUID§7: §b%s
                                §bType§7: §b%s
                                %s
                                §bTimestamp§7:
                                  §b%s
                                  %s ms
                                  
                                     """
                                .formatted(uuid, material.name(), actionInfo, DETAILED_FORMAT.format(new Date(timestamp)), timestamp)))
                .build();
    }

}
