package com.unforbidable.tfc.bids.Render.Item;

import com.dunk.tfc.Render.Item.MudBrickItemRenderer;
import net.minecraft.item.ItemStack;

public class DryingMudBrickItemRenderer extends MudBrickItemRenderer {

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		// Call super while pretending the item has "wet mud brick" damage
		ItemStack wetItem = item.copy();
		wetItem.setItemDamage(item.getItemDamage() + 32);

		super.renderItem(type, wetItem, data);
	}

}
