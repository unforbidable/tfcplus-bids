package com.unforbidable.tfc.bids.features.device.crucible.container;

import com.unforbidable.tfc.bids.features.device.crucible.container.slot.SlotCrucibleInput;
import com.unforbidable.tfc.bids.features.device.crucible.container.slot.SlotCrucibleLiquidInput;
import com.unforbidable.tfc.bids.features.device.crucible.container.slot.SlotCrucibleOutput;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityClayCrucible;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class ContainerClayCrucible extends ContainerCrucible {

    public ContainerClayCrucible(InventoryPlayer inventory, TileEntityClayCrucible te, World world, int x, int y,
            int z) {
        super(inventory, te, world, x, y, z);
    }

    @Override
    protected void buildLayout() {
        int inputSlotOffsetX = 44;
        int inputSlotOffsetY = 26;
        int inputSlotStrideX = 18;
        int inputSlotStrideY = 18;
        int inputSlotRows = 2;
        int inputSlotColumns = 2;
        int liquidInputSlotOffsetX = 107;
        int liquidInputSlotOffsetY = 17;
        int outputSlotOffsetX = 107;
        int outputSlotOffsetY = 53;

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
