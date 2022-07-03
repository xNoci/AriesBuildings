package net.tttproject.ariesbuildings.listeners;

import com.fastasyncworldedit.core.configuration.Settings;
import com.fastasyncworldedit.core.event.extent.PasteEvent;
import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import net.tttproject.ariesbuildings.blockhistory.BlockHistoryAction;
import net.tttproject.ariesbuildings.blockhistory.BlockHistoryManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class WorldEditListener {

    public WorldEditListener() {
        Settings.settings().EXTENT.ALLOWED_PLUGINS.add(WorldEditLogExtent.class.getName());
    }

    @Subscribe
    public void handlePaste(PasteEvent event) {
        if (event.getActor() == null || !event.getActor().isPlayer()) return;
        Clipboard clipboard = event.getClipboard();

        World world = BukkitAdapter.adapt(((EditSession) event.getExtent()).getWorld());
        UUID uuid = event.getActor().getUniqueId();

        BlockVector3 to = event.getPosition();
        BlockVector3 origin = clipboard.getOrigin();
        // To must be relative to the clipboard origin ( player location - clipboard origin ) (as the locations supplied are relative to the world origin)
        final int relx = to.getBlockX() - origin.getBlockX();
        final int rely = to.getBlockY() - origin.getBlockY();
        final int relz = to.getBlockZ() - origin.getBlockZ();

        for (BlockVector3 pos : clipboard) {
            BaseBlock block = pos.getFullBlock(clipboard);
            int xx = pos.getX() + relx;
            int yy = pos.getY() + rely;
            int zz = pos.getZ() + relz;

            BlockVector3 toLocation = MutableBlockVector3.get(xx, yy, zz);
            Location location = BukkitAdapter.adapt(world, toLocation);
            Material material = BukkitAdapter.adapt(block.getBlockType().getItemType());

            BlockHistoryManager.instance().updateHistory(uuid, BlockHistoryAction.WORLD_EDIT_PASTE, material, location);
        }
    }

    @Subscribe
    public void handlePaste(EditSessionEvent event) {
        if (event.getActor() == null) return;

        event.setExtent(new WorldEditLogExtent(event.getExtent(), BukkitAdapter.adapt(event.getWorld()), event.getActor()));
    }

    private static class WorldEditLogExtent extends AbstractDelegateExtent {

        private final World world;
        private final Actor actor;

        public WorldEditLogExtent(Extent extent, World world, Actor actor) {
            super(extent);

            this.world = world;
            this.actor = actor;
        }

        @Override
        public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T block) throws WorldEditException {
            onBlockChange(position, block);
            return super.setBlock(position, block);
        }

        protected <B extends BlockStateHolder<B>> void onBlockChange(BlockVector3 blockVector, B block) {
            Location location = BukkitAdapter.adapt(world, blockVector);
            Material newMaterial = BukkitAdapter.adapt(block.getBlockType().getItemType());
            UUID uuid = actor.getUniqueId();

            BlockHistoryManager.instance().updateHistory(uuid, BlockHistoryAction.WORLD_EDIT_SET, newMaterial, location);
        }

    }

}
