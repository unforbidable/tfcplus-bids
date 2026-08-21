package com.unforbidable.tfc.bids.features.utility.diggingstick.item;

import com.dunk.tfc.Items.Tools.ItemTerraTool;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemDiggingStick extends ItemTerraTool {

    public static final Set<Block> effectiveAgainstBlocks = new HashSet<>();

    private final float damageVsEntity;


    public ItemDiggingStick(ToolMaterial material) {
        super(0, material, effectiveAgainstBlocks);

        damageVsEntity = material.getDamageVsEntity() * 0.25f;

        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxDamage(material.getMaxUses());
        setNoRepair();
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":tools/" + name);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack is) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", damageVsEntity, 0));
        return multimap;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        return 100;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.block;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!player.isUsingItem() && player.isSneaking()) {
            MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mop.sideHit == 1) {
                if (canPlacePeg(is, world, mop.blockX, mop.blockY + 1, mop.blockZ)) {
                    player.setItemInUse(is, (int) (getMaxItemUseDuration(is) / efficiencyOnProperMaterial));
                }
            }
        }

        return is;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (player.isSneaking()) {
            if (count == 1) {
                MovingObjectPosition mop = getMovingObjectPositionFromPlayer(player.worldObj, player, false);
                if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mop.sideHit == 1) {
                    if (canPlacePeg(stack, player.worldObj, mop.blockX, mop.blockY + 1, mop.blockZ)) {
                        player.worldObj.playSoundEffect(mop.blockX + 0.5F, mop.blockY + 0.5F, mop.blockZ + 0.5F,
                            "dig.wood", 0.4F + (player.worldObj.rand.nextFloat() / 2), 0.7F + player.worldObj.rand.nextFloat());

                        if (!player.worldObj.isRemote) {
                            placePeg(stack, player.worldObj, mop.blockX, mop.blockY + 1, mop.blockZ);
                        }

                        if (!player.capabilities.isCreativeMode) {
                            stack.stackSize--;
                        }
                    }
                }
            }
        } else {
            player.stopUsingItem();
        }
    }

    protected boolean canPlacePeg(ItemStack stack, World world, int x, int y, int z) {
        return stack.getItem() == BidsItems.hardenedDiggingStick && world.isAirBlock(x, y, z) && BidsBlocks.woodenPeg.canBlockStay(world, x, y, z);
    }

    protected void placePeg(ItemStack stack, World world, int x, int y, int z) {
        int metadata = getMetadata(stack.getItemDamage());
        Bids.LOG.info("Placed digging stick, damage: {} -> meta: {}", stack.getItemDamage(), metadata);
        world.setBlock(x, y, z, BidsBlocks.woodenPeg, metadata, 2);
    }

    @Override
    public int getMetadata(int damage) {
        if (damage > 0) {
            // Damage is rounded up to the closest metadata value, which means some durability is often lost
            return (int)Math.ceil(damage / (float)BidsItems.hardenedDiggingStick.getMaxDamage() * 15);
        }

        return 0;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

}
