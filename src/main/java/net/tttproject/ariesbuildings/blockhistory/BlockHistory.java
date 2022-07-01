package net.tttproject.ariesbuildings.blockhistory;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.tttproject.ariesbuildings.AriesBuildings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BlockHistory {

    private static final int ENTRIES_PER_PAGE = 4;

    @Getter private final Location location;

    private ArrayList<BlockHistoryEntry> entries = Lists.newArrayList();

    public BlockHistory(Location location) {
        this.location = location;
    }

    public List<BlockHistoryEntry> getEntries() {
        // TODO Remove entries after 2 weeks; using cron jobs
        return entries.stream().sorted(Comparator.comparingLong(BlockHistoryEntry::getTimestamp).reversed()).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addEntry(UUID uuid, BlockHistoryAction action, Material material, long timestamp) {
        entries.add(new BlockHistoryEntry(uuid, action, material, timestamp));
    }

    public ItemStack getBookItem() {
        List<BaseComponent[]> pages = Lists.newArrayList();
        pages.add(getInfoPage());

        if (entries.size() == 0) {
            pages.add(getEmptyHistoryPage());
        } else {
            pages.addAll(getEntryPages());
        }

        if (entries.size() > 1) {
            pages.add(getLastPage());
        }

        ItemStack itemStack = BookUtil.writtenBook()
                .title("Block History")
                .author(AriesBuildings.getInstance().getName())
                .pages(pages)
                .build();

        return itemStack;
    }


    private BaseComponent[] getInfoPage() {
        return new BookUtil.PageBuilder()
                .newLine()
                .add(BookUtil.TextBuilder.of("   Block History").style(ChatColor.BOLD).build()).newLine()
                .newLine()
                .newLine()
                .newLine()
                .add("  Location:").newLine()
                .add("    §cX §8» §c" + location.getBlockX()).newLine()
                .add("    §aY §8» §a" + location.getBlockY()).newLine()
                .add("    §9Z §8» §9" + location.getBlockZ()).newLine()
                .newLine()
                .newLine()
                .newLine()
                .newLine()
                .add(BookUtil.TextBuilder.of(AriesBuildings.getInstance().getName())
                        .color(ChatColor.DARK_GRAY)
                        .style(ChatColor.ITALIC)
                        .onHover(BookUtil.HoverAction
                                .showText("§fPlugin by "
                                        + String.join(", ",
                                        AriesBuildings.getInstance().getDescription().getAuthors())
                                        + ".")).build())
                .build();
    }

    private BaseComponent[] getEmptyHistoryPage() {
        return new BookUtil.PageBuilder()
                .newLine()
                .newLine()
                .newLine()
                .newLine()
                .newLine()
                .newLine()
                .add(BookUtil.TextBuilder.of("   [Empty History]")
                        .color(ChatColor.RED)
                        .style(ChatColor.ITALIC)
                        .onHover(BookUtil.HoverAction
                                .showText("This block does not contain any history information.")).build())
                .build();
    }

    private BaseComponent[] getLastPage() {
        return new BookUtil.PageBuilder()
                .newLine()
                .newLine()
                .newLine()
                .newLine()
                .add(BookUtil.TextBuilder.of("  [End of History]")
                        .style(ChatColor.ITALIC)
                        .onClick(BookUtil.ClickAction.changePage(1))
                        .onHover(BookUtil.HoverAction.showText(BookUtil.TextBuilder.of("Click here to get to the first page.").color(ChatColor.WHITE).style(ChatColor.ITALIC).build())).build()).newLine()
                .newLine()
                .newLine()
                .newLine()
                .newLine()
                .add(BookUtil.TextBuilder.of("[Click here to get to the first page]")
                        .style(ChatColor.ITALIC)
                        .onClick(BookUtil.ClickAction.changePage(1))
                        .onHover(BookUtil.HoverAction.showText(BookUtil.TextBuilder.of("Click here to get to the first page.").color(ChatColor.WHITE).style(ChatColor.ITALIC).build())).build())
                .build();
    }

    private List<BaseComponent[]> getEntryPages() {
        List<BlockHistoryEntry> entries = getEntries();
        List<BaseComponent[]> pages = Lists.newArrayList();
        BookUtil.PageBuilder currentPage = BookUtil.PageBuilder.of("");

        for (int i = 0; i < entries.size(); i++) {
            BlockHistoryEntry currentEntry = entries.get(i);
            currentPage.add(currentEntry.getBookText()).newLine().newLine();

            if ((i + 1) % ENTRIES_PER_PAGE == 0) {
                pages.add(currentPage.build());
                currentPage = BookUtil.PageBuilder.of("");
            }
        }
        return pages;
    }

}
