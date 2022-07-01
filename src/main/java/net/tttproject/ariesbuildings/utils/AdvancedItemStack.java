package net.tttproject.ariesbuildings.utils;

import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdvancedItemStack extends ItemStack {

    public AdvancedItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    public AdvancedItemStack(Material material) {
        super(material);
    }

    public AdvancedItemStack(Material material, String displayName) {
        super(material, 1);
        setDisplayName(displayName);
    }

    public AdvancedItemStack(Material material, int amount) {
        super(material, amount);
    }

    public static AdvancedItemStack fromBase64(String base64) {
        try {
            return new AdvancedItemStack(ItemSerializer.itemStackFromBase64(base64));
        } catch (IOException e) {
            throw new RuntimeException("Unable to decode class type.", e);
        }
    }

    public String toBase64() {
        return ItemSerializer.itemStackToBase64(this);
    }

    public void setNBTTag(String key, NBTBase value) {
        ItemMeta m = this.getItemMeta();
        String stored = "";
        if (m.getDisplayName() != null) {
            stored = m.getDisplayName();
        }
        m.setDisplayName("nbt test");
        this.setItemMeta(m);

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);

        NBTTagCompound compound = nmsItem.getTag();

        compound.set(key, value);
        nmsItem.setTag(compound);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        meta.setDisplayName(stored);
        this.setItemMeta(meta);
    }

    public void setNBTTag(String key, String value) {
        setNBTTag(key, NBTTagString.a(value));
    }


    public void setNBTBool(String key, boolean value) {
        ItemMeta m = this.getItemMeta();
        String stored = "";
        if (m.getDisplayName() != null) {
            stored = m.getDisplayName();
        }
        m.setDisplayName("nbt test");
        this.setItemMeta(m);

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);

        NBTTagCompound compound = nmsItem.getTag();

        compound.setBoolean(key, value);
        nmsItem.setTag(compound);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        meta.setDisplayName(stored);
        this.setItemMeta(meta);
    }

    public void removeNBTTag(String key) {
        ItemMeta m = this.getItemMeta();
        String stored = "";
        if (m.getDisplayName() != null) {
            stored = m.getDisplayName();
        }
        m.setDisplayName("nbt test");
        this.setItemMeta(m);

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);

        NBTTagCompound compound = nmsItem.getTag();

        compound.remove(key);
        nmsItem.setTag(compound);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        meta.setDisplayName(stored);
        this.setItemMeta(meta);
    }

    public NBTBase getNBTTag(String key) {
        try {
            net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);
            NBTTagCompound compound = nmsItem.getTag();
            return compound.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public String getNBTTagString(String key) {
        try {
            net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(this);
            NBTTagCompound compound = item.getTag() != null ? item.getTag() : new NBTTagCompound();
            String value = compound.getString(key);
            return value.isEmpty() ? null : value;
        } catch (Exception e) {
            return null;
        }
    }

    public int getNBTTagInt(String key, int defaultValue) {
        try {
            net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(this);
            NBTTagCompound tag = item.getTag() != null ? item.getTag() : new NBTTagCompound();
            return tag.getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getNBTTagBoolean(String key) {
        try {
            net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(this);
            NBTTagCompound tag = item.getTag() != null ? item.getTag() : new NBTTagCompound();
            return tag.getBoolean(key);
        } catch (Exception e) {
            return false;
        }
    }

    public String getDisplayName() {
        return this.getItemMeta().getDisplayName();
    }

    public AdvancedItemStack setDisplayName(String name) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(name);
        this.setItemMeta(itemMeta);
        return this;
    }

    public AdvancedItemStack setMeta(ItemMeta itemMeta) {
        this.setItemMeta(itemMeta);
        return this;
    }

    public AdvancedItemStack setLore(List<String> lore) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
        return this;
    }

    public AdvancedItemStack setLore(String... lore) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        this.setItemMeta(itemMeta);
        return this;
    }

    public AdvancedItemStack addEnchantment(Enchantment enchantment, long level) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(enchantment, (int) level, false);
        this.setItemMeta(itemMeta);
        return this;
    }

    public AdvancedItemStack addItemFlags() {
        return addItemFlag(ItemFlag.values());
    }

    public AdvancedItemStack addItemFlag(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.setItemMeta(itemMeta);
        return this;
    }

    public AdvancedItemStack setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        this.setItemMeta(itemMeta);
        return this;
    }

    @Override
    @Deprecated
    public void addEnchantment(Enchantment enchantment, int level) {
        addUnsafeEnchantment(enchantment, level);
    }

    public AdvancedItemStack setUnsafeEnchantment(Enchantment ench, long level) {
        addUnsafeEnchantment(ench, (int) level);
        return this;
    }


    public AdvancedItemStack setSkullOwner(String name) {
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwner(name);
        this.setItemMeta(skullMeta);
        return this;
    }

    public AdvancedItemStack setTitle(String title) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setTitle(title);
        this.setItemMeta(bookMeta);
        return this;
    }

    public AdvancedItemStack addPage(List<String> pageContent) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.addPage(pageContent.toString());
        this.setItemMeta(bookMeta);
        return this;
    }

    public AdvancedItemStack setAuthor(String author) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setAuthor(author);
        this.setItemMeta(bookMeta);
        return this;
    }

    public AdvancedItemStack setBookGeneration(BookMeta.Generation generation) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setGeneration(generation);
        this.setItemMeta(bookMeta);
        return this;
    }

    public AdvancedItemStack setBookTitle(String title) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setTitle(title);
        this.setItemMeta(bookMeta);
        return this;
    }

    public AdvancedItemStack addPage(String... pageContent) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.addPage(pageContent);
        this.setItemMeta(bookMeta);
        return this;
    }

    public AdvancedItemStack setBookEnchantment(Enchantment enchantment, int level) {
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) this.getItemMeta();
        enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
        this.setItemMeta(enchantmentStorageMeta);
        return this;
    }

    public AdvancedItemStack setLeatherColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.setItemMeta(leatherArmorMeta);
        return this;
    }

    public String getRawDisplayName() {
        return ChatColor.stripColor(getDisplayName());
    }

    public boolean isNull() {
        return getType() == Material.AIR;
    }

}
