package com.unforbidable.tfc.bids.Core.ProcessingSurface;

import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Items.Tools.ItemWeapon;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ProcessingSurfaceHelper {

    private static final Map<Integer, IIcon> icons = new HashMap<Integer, IIcon>();

    public static IIcon getIconForItem(ItemStack itemStack) {
        int key = getItemKey(itemStack);
        if (icons.containsKey(key)) {
            return icons.get(key);
        } else {
            return TFC_Textures.invisibleTexture;
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

    public static void registerProcessingSurfaceRecipeIcons(IIconRegister registerer) {
        for (ProcessingSurfaceRecipe recipe : ProcessingSurfaceManager.getRecipes()) {
            ItemStack input = recipe.getInput();
            ItemStack result = recipe.getResult(input);

            registerProcessingSurfaceItemIcon(registerer, input);
            registerProcessingSurfaceItemIcon(registerer, result);
        }
    }

    private static void registerProcessingSurfaceItemIcon(IIconRegister registerer, ItemStack itemStack) {
        int key = getItemKey(itemStack);
        if (!icons.containsKey(key)) {
            IIcon providedIcon = ProcessingSurfaceManager.registerIcon(registerer, itemStack);
            if (providedIcon != null) {
                icons.put(key, providedIcon);
            } else {
                // When icon is not provided,
                // look for the block icon in the same path as where item icon is
                IIcon icon = registerer.registerIcon(getIconName(itemStack));
                icons.put(key, icon);
            }
        }
    }

    private static String getIconName(ItemStack itemStack) {
        return itemStack.getIconIndex().getIconName();
    }

    private static int getItemKey(ItemStack itemStack) {
        return Item.getIdFromItem(itemStack.getItem()) << 16 + itemStack.getItemDamage();
    }

}
