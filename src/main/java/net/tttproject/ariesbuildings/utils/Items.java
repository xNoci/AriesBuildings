package net.tttproject.ariesbuildings.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Items {

    BLOCK_HISTORY(Material.BLAZE_ROD, "§cBlock History", "", "§7By right clicking a block you can", "§7see the history of a block.");

    private final Material material;
    private final String displayName;
    @Getter private final AdvancedItemStack itemStack;

    Items(Material material, String displayName, String... lore) {
        this.material = material;
        this.displayName = displayName;
        this.itemStack = new AdvancedItemStack(this.material).setLore(lore).setDisplayName(this.displayName).addItemFlags();
    }

    Items(AdvancedItemStack itemStack, String displayName, String... lore) {
        this.itemStack = itemStack;
        this.material = itemStack.getType();
        this.displayName = displayName;

        this.itemStack.setDisplayName(displayName);
        this.itemStack.setLore(lore);
        this.itemStack.addItemFlags();
    }

    public boolean isSameItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType() != material) return false;
        if (!itemStack.hasItemMeta()) return false;
        if (!itemStack.getItemMeta().hasDisplayName()) return false;
        return itemStack.getItemMeta().getDisplayName().equals(displayName);
    }

    public static boolean isItem(ItemStack itemStack) {
        for (Items value : values()) {
            if (value.isSameItem(itemStack)) {
                return true;
            }
        }
        return false;
    }

}
