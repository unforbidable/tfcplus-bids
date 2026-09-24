package com.unforbidable.tfc.bids.features.utility.hidevessel.item;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.IBag;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.meta.HideVesselMeta;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import com.unforbidable.tfc.bids.util.ItemHelper;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ItemHideVesselFluid extends ItemTerra implements IBag {

    IIcon overlayIcon;

    public ItemHideVesselFluid() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setFolder("tools");
        setHasSubtypes(true);
        setMaxDamage(HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE);
    }

    @Override
    public boolean onUpdate(ItemStack is, World world, int x, int y, int z) {
        if (!world.isRemote) {
            HideVesselHelper.damageHideVessel(is, HideVesselHelper.HIDE_VESSEL_FILLED_DAMAGE_INTERVAL);
        }

        return true;
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
                             float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (side == 1) {
                if (CookingPitHelper.placeHideCookingPitAt(world, x, y + 1, z, is, player)) {
                    is.stackSize--;
                }
            }
        } else {
            // Only fresh water and salt water can be emptied in the world
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(is);
            if (fluidStack != null) {
                Fluid fluid = fluidStack.getFluid();
                if (fluid == TFCFluids.FRESHWATER || fluid == TFCFluids.SALTWATER) {
                    ForgeDirection d = ForgeDirection.getOrientation(side);
                    int x2 = x + d.offsetX;
                    int y2 = y + d.offsetY;
                    int z2 = z + d.offsetZ;

                    if (world.isAirBlock(x2, y2, z2)) {
                        world.setBlock(x2, y2, z2, fluid == TFCFluids.FRESHWATER ? TFCBlocks.freshWater : TFCBlocks.saltWater, 2, 0x1);

                        ItemStack empty = FluidContainerRegistry.drainFluidContainer(is);
                        player.setCurrentItemOrArmor(0, empty);

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return stack.getItemDamage() >= HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getContainerItem().getUnlocalizedName().replace("item.", "") + ".Overlay");
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata) {
        return metadata != HideVesselMeta.RUINED ? 2 : 1;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1) {
            return overlayIcon;
        } else {
            return getContainerItem().getIcon(stack, pass);
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 1) {
            return FluidContainerRegistry.getFluidForFilledItem(stack).getFluid().getColor();
        } else {
            return super.getColorFromItemStack(stack, pass);
        }
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        if (ItemHelper.showShiftInformation()) {
            arraylist.add(StatCollector.translateToLocal("gui.Help"));
            arraylist.add(StatCollector.translateToLocal("gui.Help.HideVessel.Filled"));
        } else {
            arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

    @Override
    public ItemStack[] loadBagInventory(ItemStack itemStack) {
        return new ItemStack[0];
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.MEDIUM;
    }

}
