package com.unforbidable.tfc.bids.api.features.firestarting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class FireStartingEvent extends PlayerEvent {

    public enum Stage {
        /**
         * Checking if fire can be started at given location.
         * Triggered before and repeatedly during firestarter usage and sometimes also before ignition.
         * <p>
         * Value of <code>result</code> indicates whether block can be ignited.
         * Set cancelled to prevent ignition even when other handlers should return a positive check.
         * <p>
         * Both client and server side.
         */
        START,
        /**
         * When one fire starting cycle is completed, whether successfully or not.
         * <p>
         * Value of <code>result</code> indicates whether the fire starting is allowed to complete.
         * <p>
         * Server side only.
         */
        COMPLETE,
        /**
         * For actual ignition of a specific block.
         * Typically triggered at the end of a successful fire starting cycle.
         * <p>
         * Value of <code>result</code> indicates whether the block was actually ignited.
         * Unsuccessful ignition will instead spawn Smoldering Embers if tinder was used.
         * <p>
         * Using Smoldering Embers to ignite a block (by right-clicking or dropping) may also trigger this event.
         * When dropped Smoldering Embers ignite a block, the value of <code>entityPlayer</code> is <code>null</code>.
         * <p>
         * Server side only.
         */
        IGNITE,
        /**
         * Checking whether tinder can catch fire from a specific block.
         * Value of <code>result</code> indicates positive result.
         * Set cancelled to prevent tinder ignition even when other handlers should return a positive check.
         */
        PROPAGATE,
    }

    public final World world;
    public final int x;
    public final int y;
    public final int z;
    public final int side;
    public final Stage stage;

    public boolean result;

    public FireStartingEvent(EntityPlayer player, World world, int x, int y, int z, int side, Stage stage) {
        super(player);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.stage = stage;
    }

}
