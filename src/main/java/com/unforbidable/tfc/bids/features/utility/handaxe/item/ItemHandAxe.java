package com.unforbidable.tfc.bids.features.utility.handaxe.item;

import com.dunk.tfc.Blocks.Flora.BlockBranch;
import com.dunk.tfc.Items.Tools.ItemTerraTool;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.Tools.IKnife;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

public class ItemHandAxe extends ItemTerraTool implements ISize, IKnife {

    public static final Set<Block> effectiveAgainstBlocks = new HashSet<>();

    private final float damageVsEntity;

    public ItemHandAxe(ToolMaterial material) {
        super(0, material, effectiveAgainstBlocks);

        damageVsEntity = material.getDamageVsEntity() * 0.5f;

        setCreativeTab(BidsCreativeTabs.bidsTools);
        setNoRepair();
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return Math.max(1, (isBlockActualBranch(block) ? efficiencyOnProperMaterial : super.getDigSpeed(stack, block, meta)) * 0.5f);
    }

    private boolean isBlockActualBranch(Block block) {
        if (block instanceof BlockBranch) {
            BlockBranch blockBranch = (BlockBranch) block;
            return blockBranch.getSourceX() != 0 || blockBranch.getSourceZ() != 0;
        }

        return false;
    }

    @Override
    public int getMaxDamage(ItemStack is) {
        return (int) Math.floor(getMaxDamage() + (getMaxDamage() * AnvilManager.getDurabilityBuff(is)));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack is) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", getWeaponDamage(is), 0));
        return multimap;
    }

    private double getWeaponDamage(ItemStack is) {
        return Math.floor(damageVsEntity + (damageVsEntity * AnvilManager.getDamageBuff(is)));
    }


    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "")
                .replace("IgIn ", "").replace("IgEx ", "").replace("Sed ", "").replace("MM ", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":tools/" + name);
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
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

}
