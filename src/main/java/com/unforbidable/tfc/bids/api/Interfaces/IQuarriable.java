package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumQuarryEquipmentTier;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IQuarriable {

    Block getRawBlock();

    Block getQuarriedBlock();

    boolean isSufficientEquipmentTier(Block block, int metadata, EnumQuarryEquipmentTier equipmentTier);

    boolean canQuarryBlock(Block block, int metadata);

    boolean canQuarryBlockAt(World world, int x, int y, int z, int side);

    boolean isQuarryReady(World world, int x, int y, int z);

    boolean blockRequiresWedgesToDetach(Block block, int metadata);

    int getDrillDamage(Block block, int metadata);

    float getDrillDurationMultiplier(Block block, int metadata);

    int getQuarriedBlockMetadata(Block block, int metadata);

}
