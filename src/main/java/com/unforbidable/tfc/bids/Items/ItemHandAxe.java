package com.unforbidable.tfc.bids.Items;

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
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

public class ItemHandAxe extends ItemTool implements ISize, IKnife {

    private static final Set<Block> BLOCKS_EFFECTIVE_AGAINST = Sets.newHashSet();

    private final float damageVsEntity;

    public ItemHandAxe(ToolMaterial material) {
        super(0, material, BLOCKS_EFFECTIVE_AGAINST);

        damageVsEntity = material.getDamageVsEntity() * 0.5f;

        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxDamage(material.getMaxUses() * 3);
        setNoRepair();
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return super.getDigSpeed(stack, block, meta) * 0.5f;
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
