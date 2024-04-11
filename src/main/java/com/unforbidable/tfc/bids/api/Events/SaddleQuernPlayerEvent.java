package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.Core.SaddleQuern.EnumWorkStoneType;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class SaddleQuernPlayerEvent extends PlayerEvent {

    public enum Action {
        PLACE_WORK_STONE,
        RETRIEVE_WORK_STONE,
        USE_WORK_STONE,
        PLACE_LEVER,
        ATTACH_WEIGHT_STONE,
        DETACH_WEIGHT_STONE
    }

    public final TileEntitySaddleQuern tileEntitySaddleQuern;
    public final Action action;
    public final EnumWorkStoneType workStoneType;

    public SaddleQuernPlayerEvent(EntityPlayer player, TileEntitySaddleQuern tileEntitySaddleQuern, Action action, EnumWorkStoneType workStoneType) {
        super(player);

        this.tileEntitySaddleQuern = tileEntitySaddleQuern;
        this.action = action;
        this.workStoneType = workStoneType;
    }

    public SaddleQuernPlayerEvent(EntityPlayer player, TileEntitySaddleQuern tileEntitySaddleQuern, Action action) {
        this(player, tileEntitySaddleQuern, action, EnumWorkStoneType.NONE);
    }


}
