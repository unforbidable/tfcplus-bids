package com.unforbidable.tfc.bids.features.utility.diggingstick.item;

import com.dunk.tfc.Items.Tools.ItemTerraTool;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
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
