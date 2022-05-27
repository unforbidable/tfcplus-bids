package com.unforbidable.tfc.bids.Containers;

import com.unforbidable.tfc.bids.Containers.Slots.SlotCrucibleInput;
import com.unforbidable.tfc.bids.Containers.Slots.SlotCrucibleLiquidInput;
import com.unforbidable.tfc.bids.Containers.Slots.SlotCrucibleOutput;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class ContainerFireClayCrucible extends ContainerCrucible {

    public ContainerFireClayCrucible(InventoryPlayer inventory, TileEntityCrucible te, World world, int x, int y,
            int z) {
        super(inventory, te, world, x, y, z);
    }

    @Override
    protected void buildLayout() {
        int inputSlotOffsetX = 26;
        int inputSlotOffsetY = 17;
        int liquidInputSlotOffsetX = 125;
        int liquidInputSlotOffsetY = 17;
        int outputSlotOffsetX = 125;
        int outputSlotOffsetY = 53;
        int inputSlotStrideX = 18;
        int inputSlotStrideY = 18;
        int inputSlotRows = 3;
        int inputSlotColumns = 4;

        int i = 0;
        for (int iy = 0; iy < inputSlotRows; iy++) {
            for (int ix = 0; ix < inputSlotColumns; ix++) {
                addSlotToContainer(new SlotCrucibleInput(crucibleTileEntity, i++,
                        inputSlotOffsetX + ix * inputSlotStrideX, inputSlotOffsetY + iy * inputSlotStrideY));
            }
        }
        addSlotToContainer(new SlotCrucibleLiquidInput(crucibleTileEntity, i++,
                liquidInputSlotOffsetX, liquidInputSlotOffsetY));
        addSlotToContainer(new SlotCrucibleOutput(crucibleTileEntity, i++,
                outputSlotOffsetX, outputSlotOffsetY));
    }

}
