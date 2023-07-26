package com.unforbidable.tfc.bids.Items;

import java.util.List;
import java.util.Set;

import com.dunk.tfc.Items.Tools.ItemTerraTool;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Carving.CarvingHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemAdze extends ItemTerraTool implements ISize, ICarvingTool {

    private static final Set<Block> BLOCKS_EFFECTIVE_AGAINST = Sets.newHashSet(BidsBlocks.carvingRock, BidsBlocks.roughStoneSed, BidsBlocks.roughStoneBrickSed);
    private final float damageVsEntity;

    public ItemAdze(ToolMaterial material) {
        super(0, material, BLOCKS_EFFECTIVE_AGAINST);

        damageVsEntity = material.getDamageVsEntity() * 0.5f;

        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxDamage(material.getHarvestLevel() < 2 ? material.getMaxUses() * 2 : material.getMaxUses());
        setNoRepair();
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
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        if (ItemHelper.showShiftInformation()) {
            arraylist.add(StatCollector.translateToLocal("gui.Help"));
            arraylist.add(StatCollector.translateToLocal("gui.Help.Adze"));
            arraylist.add(StatCollector.translateToLocal("gui.Help.Adze2"));
            arraylist.add(StatCollector.translateToLocal("gui.Help.Adze3"));
        } else {
            arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (CarvingHelper.carveBlockAt(world, player, x, y, z, side, hitX, hitY, hitZ, this)) {
            return true;
        }

        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

}
