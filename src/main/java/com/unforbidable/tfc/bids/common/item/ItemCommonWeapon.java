package com.unforbidable.tfc.bids.common.item;

import com.dunk.tfc.Items.Tools.ItemWeapon;
import com.dunk.tfc.api.Enums.EnumDamageType;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemCommonWeapon extends ItemWeapon {

    public ItemCommonWeapon(ToolMaterial material, float damage, EnumDamageType damageType) {
        super(material, damage);

        this.damageType = damageType;

        setCreativeTab(BidsCreativeTabs.bidsTools);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "tools/" +
            this.getUnlocalizedName().replace("item.", ""));
    }

}
