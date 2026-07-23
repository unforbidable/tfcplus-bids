package com.unforbidable.tfc.bids.features.crafting.threshing.main;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.features.threshing.ThreshingFloor;
import com.unforbidable.tfc.bids.api.features.threshing.ThreshingRecipe;
import com.unforbidable.tfc.bids.features.crafting.threshing.ThreshingRegistry;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import com.unforbidable.tfc.bids.util.playerstate.PlayerStateManager;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ThreshingHelper {

    public static boolean thresh(World world, int x, int y, int z, EntityPlayer player, ItemStack tool) {
        if (isPlayerReadyToThresh(player) &&
            isValidThreshingTool(tool) &&
            isValidThreshingSurfaceAt(world, x, y, z)) {
            for (EntityItem entityItem : getEntityItemNearBy(world, x, y, z)) {
                ItemStack input = entityItem.getEntityItem();
                ThreshingRecipe recipe = ThreshingRegistry.recipes.findMatchingRecipe(input);
                if (recipe != null) {
                    if (!world.isRemote) {
                        player.worldObj.playSoundAtEntity(player, "step.grass", 1F, 1F + world.rand.nextFloat() * 0.4F);

                        float inputWeight = Food.getWeight(input);
                        float inputDecay = Food.getDecay(input);
                        float consumeWeight = Food.getWeight(recipe.getInput());
                        float produceWeight = Food.getWeight(recipe.getOutput());
                        float produceWeightRatio = produceWeight / consumeWeight;

                        float actualConsumeWeight = Math.min(inputWeight - inputDecay, consumeWeight);
                        if (inputWeight - inputDecay - actualConsumeWeight < 2) {
                            actualConsumeWeight = inputWeight - inputDecay;
                        }

                        if (inputWeight - inputDecay - actualConsumeWeight > 0) {
                            Food.setWeight(input, inputWeight - actualConsumeWeight);
                        } else {
                            input.stackSize--;
                        }

                        float actualProduceWeight = actualConsumeWeight * produceWeightRatio;

                        ItemStack ingredient = input.copy();
                        Food.setWeight(ingredient, actualConsumeWeight);
                        ItemStack result = recipe.getResult(input);
                        Food.setWeight(result, actualProduceWeight);
                        Food.setDecayTimer(result, (int) TFC_Time.getTotalHours() + 1);

                        ItemStack extraResult = recipe.getExtraResult(ingredient);

                        BidsEventFactory.onThreshingItemCrafted(player, ingredient, result, extraResult, tool);

                        ThreshingFloor threshingFloor = getThreshingFloor(world, x, y, z);

                        if (threshingFloor == null || !threshingFloor.takeGrain(result)) {
                            EntityItem resultEntityItem = new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, result);
                            resultEntityItem.delayBeforeCanPickup = 20;
                            world.spawnEntityInWorld(resultEntityItem);
                        }

                        if (extraResult != null) {
                            if (threshingFloor == null || !threshingFloor.takeStraw(extraResult)) {
                                EntityItem extraEntityItem = new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, extraResult);
                                extraEntityItem.delayBeforeCanPickup = 20;
                                world.spawnEntityInWorld(extraEntityItem);
                            }
                        }

                        if (entityItem.getEntityItem().stackSize == 0) {
                            entityItem.delayBeforeCanPickup = 100;
                            entityItem.setInvisible(true);
                            entityItem.setDead();
                        } else {
                            entityItem.delayBeforeCanPickup = 100;
                            entityItem.lifespan = entityItem.age + 6000;
                        }

                        tool.damageItem(1, player);
                        if (tool.stackSize == 0) {
                            world.playSoundEffect(player.posX, player.posY, player.posZ, "random.break",
                                0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());
                        }

                        setPlayerThreshingDelay(player, Math.round(recipe.getDuration() * getToolDurationMultiplier(tool)));
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private static void setPlayerThreshingDelay(EntityPlayer player, int duration) {
        ThreshingPlayerState state = new ThreshingPlayerState();
        state.nextThreshingTick = TFC_Time.getTotalTicks() + duration;
        PlayerStateManager.setPlayerState(player, state);
    }

    private static boolean isPlayerReadyToThresh(EntityPlayer player) {
        ThreshingPlayerState state = PlayerStateManager.getPlayerState(player, ThreshingPlayerState.class);
        return state == null || state.nextThreshingTick < TFC_Time.getTotalTicks();
    }

    private static float getToolDurationMultiplier(ItemStack tool) {
        if (OreDictionaryHelper.itemStackIsOre(tool, "itemPrimitiveTool")) {
            return 4;
        } else {
            return 1;
        }
    }

    private static boolean isValidThreshingTool(ItemStack tool) {
        return OreDictionaryHelper.itemStackIsOre(tool, "itemThreshingTool");
    }

    private static boolean isValidThreshingSurfaceAt(World world, int x, int y, int z) {
        if (getThreshingFloor(world, x, y, z) != null) {
            return true;
        } else {
            Block surfaceBlock = world.getBlock(x, y, z);
            int surfaceBlockMetadata = world.getBlockMetadata(x, y, z);
            ItemStack surface = new ItemStack(surfaceBlock, 1, surfaceBlockMetadata);
            return OreDictionaryHelper.itemStackIsOre(surface, "blockThreshingSurface");
        }
    }

    private static ThreshingFloor getThreshingFloor(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof ThreshingFloor) {
            return (ThreshingFloor) tileEntity;
        } else {
            return null;
        }
    }

    @SuppressWarnings({"unchecked"})
    private static List<EntityItem> getEntityItemNearBy(World world, int x, int y, int z) {
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x, y, z,
            x + 1, y + 1.2, z + 1);
        return (List<EntityItem>) world.getEntitiesWithinAABB(EntityItem.class, bounds);
    }

}
