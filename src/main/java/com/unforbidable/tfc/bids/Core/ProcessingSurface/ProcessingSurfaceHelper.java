package com.unforbidable.tfc.bids.Core.ProcessingSurface;

import com.dunk.tfc.Items.Tools.ItemWeapon;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;

import java.lang.reflect.Field;

public class ProcessingSurfaceHelper {

    public static IIcon getIconForItem(ItemStack is) {
        IIcon icon = ProcessingSurfaceManager.getIcon(is);
        if (icon != null) {
            return icon;
        } else {
            return is.getIconIndex();
        }
    }

    public static float getToolEfficiency(ItemStack tool) {
        Item item = tool.getItem();
        if (item instanceof ItemTool) {
            return getToolMaterial(item).getEfficiencyOnProperMaterial();
        } else if (item instanceof ItemWeapon) {
            return getWeaponMat(item).getEfficiencyOnProperMaterial();
        } else {
            return 1f;
        }
    }

    private static Item.ToolMaterial getToolMaterial(Item item) {
        return ((ItemTool) item).func_150913_i();
    }

    private static Item.ToolMaterial getWeaponMat(Item item) {
        try {
            Field toolMatField = ItemWeapon.class.getDeclaredField("toolMat");
            toolMatField.setAccessible(true);
            return (Item.ToolMaterial)toolMatField.get(item);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
