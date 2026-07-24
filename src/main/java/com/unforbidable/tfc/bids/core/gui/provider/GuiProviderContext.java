package com.unforbidable.tfc.bids.core.gui.provider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiProviderContext {

    public final EntityPlayer player;
    public final World world;
    public final int x;
    public final int y;
    public final int z;

    public GuiProviderContext(EntityPlayer player, World world, int x, int y, int z) {
        this.player = player;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
