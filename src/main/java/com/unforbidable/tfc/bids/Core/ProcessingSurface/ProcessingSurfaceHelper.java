package com.unforbidable.tfc.bids.Core.ProcessingSurface;

import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Items.Tools.ItemWeapon;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

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
            ItemStack itemStackWild = new ItemStack(itemStack.getItem(), 1, OreDictionary.WILDCARD_VALUE);
            int keyWild = getItemKey(itemStackWild);
            if (icons.containsKey(keyWild)) {
                return icons.get(keyWild);
            } else {
                return TFC_Textures.invisibleTexture;
            }
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

            registerSurfaceItemIcon(registerer, input);
            registerSurfaceItemIcon(registerer, result);
        }
    }

    public static void registerDecorativeSurfaceIcons(IIconRegister registerer) {
        for (ItemStack is : OreDictionary.getOres("itemDecorativeSurface")) {
            registerSurfaceItemIcon(registerer, is);
        }
    }

    private static void registerSurfaceItemIcon(IIconRegister registerer, ItemStack itemStack) {
        String blockIconName = getIconName(itemStack);
        Bids.LOG.info("Surface block texture {} will be used for item {}", blockIconName, itemStack);

        IIcon icon = registerer.registerIcon(blockIconName);
        icons.put(getItemKey(itemStack), icon);
    }

    public static int getOrientation(EntityPlayer player) {
        int dir = (int) Math.floor(player.rotationYaw * 4F / 360F + 0.5D);
        return dir & 3;
    }

    private static String getIconName(ItemStack itemStack) {
        String itemIconName = itemStack.getItem().getUnlocalizedName().replace("item.", "");
        String blockIconName = getDefaultBlockIconName(itemIconName);
        return BidsEventFactory.onSurfaceItemIcon(itemStack, blockIconName);
    }

    private static String getDefaultBlockIconName(String itemIconName) {
        int modPartEnd = itemIconName.indexOf(':');
        int filePartStart = itemIconName.lastIndexOf('/');
        String filePart = filePartStart < 0 ? itemIconName.substring(modPartEnd + 1) : itemIconName.substring(filePartStart + 1);
        return Tags.MOD_ID + ":surface/" + filePart;
    }

    private static int getItemKey(ItemStack itemStack) {
        return Item.getIdFromItem(itemStack.getItem()) << 16 + itemStack.getItemDamage();
    }

}
