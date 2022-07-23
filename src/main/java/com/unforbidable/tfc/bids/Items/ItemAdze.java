package com.unforbidable.tfc.bids.Items;

import java.util.List;
import java.util.Set;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Carving.CarvingHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemAdze extends ItemTool implements ISize, ICarvingTool {

    private static final Set<Block> BLOCKS_EFFECTIVE_AGAINST = Sets.newHashSet(new Block[] {
            BidsBlocks.carvingRock, BidsBlocks.roughStoneSed, BidsBlocks.roughStoneBrickSed
    });

    public ItemAdze(ToolMaterial material) {
        super(-0.25F * material.getDamageVsEntity(), material, BLOCKS_EFFECTIVE_AGAINST);
        setCreativeTab(BidsCreativeTabs.BidsDefault);
        setMaxDamage(material.getMaxUses());
        setNoRepair();
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
        return EnumSize.MEDIUM;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        if (ItemHelper.showShiftInformation()) {
            list.add(StatCollector.translateToLocal("gui.Help"));
            list.add(StatCollector.translateToLocal("gui.Help.Adze"));
            list.add(StatCollector.translateToLocal("gui.Help.Adze2"));
            list.add(StatCollector.translateToLocal("gui.Help.Adze3"));
        } else {
            list.add(StatCollector.translateToLocal("gui.ShowHelp"));
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
