package com.unforbidable.tfc.bids.features.utility.hidevessel.item;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.IBag;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt.HideVesselTag;
import com.unforbidable.tfc.bids.util.ItemHelper;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemHideVessel extends ItemTerra implements IBag, IFluidContainerItem {

    IIcon overlayIcon;

    public ItemHideVessel() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setFolder("tools");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(HideVesselTag.of(new ItemStack(this)).setDamage(0).stack());
        list.add(HideVesselTag.of(new ItemStack(this)).setDamage(0).setFluid(new FluidStack(TFCFluids.FRESHWATER, 1000)).stack());
    }

    @Override
    public boolean onUpdate(ItemStack is, World world, int x, int y, int z) {
        if (!world.isRemote) {
            HideVesselTag tag = HideVesselTag.of(is);
            if (tag.getLastDamageTicks() == 0) {
                tag.setLastDamageTicks(TFC_Time.getTotalTicks());
            }

            long lastDamageTicks = tag.getLastDamageTicks();
            long damageInterval = tag.hasFluid() ? HideVesselHelper.HIDE_VESSEL_FILLED_DAMAGE_INTERVAL : HideVesselHelper.HIDE_VESSEL_EMPTY_DAMAGE_INTERVAL;
            if (lastDamageTicks + damageInterval < TFC_Time.getTotalTicks()) {
                int newDamage = tag.getDamage() + 1;
                if (newDamage > HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE) {
                    is.stackSize = 0;

                    Bids.LOG.info("Hide vessel destroyed");
                } else {
                    tag.setDamage(newDamage);
                    tag.setLastDamageTicks(TFC_Time.getTotalTicks());

                    Bids.LOG.info("Hide vessel damaged {}/{}", newDamage, HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
                             float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (side == 1) {
                HideVesselTag tag = HideVesselTag.of(is);
                if (CookingPitHelper.placeHideCookingPitAt(world, x, y + 1, z, tag.getFluid(), tag.getDamage())) {
                    is.stackSize--;
                }
            }
        } else {
            // Only fresh water and salt water can be emptied in the world
            FluidStack fluidStack = HideVesselTag.of(is).getFluid();
            if (fluidStack != null) {
                Fluid fluid = fluidStack.getFluid();
                if (fluid == TFCFluids.FRESHWATER || fluid == TFCFluids.SALTWATER) {
                    ForgeDirection d = ForgeDirection.getOrientation(side);
                    int x2 = x + d.offsetX;
                    int y2 = y + d.offsetY;
                    int z2 = z + d.offsetZ;

                    if (world.isAirBlock(x2, y2, z2)) {
                        world.setBlock(x2, y2, z2, fluid == TFCFluids.FRESHWATER ? TFCBlocks.freshWater : TFCBlocks.saltWater, 2, 0x1);

                        HideVesselTag.of(is).clearFluid();

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!HideVesselTag.of(is).hasFluid()) {
            return HideVesselHelper.fillFromWorld(is, world, player);
        }

        return is;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", ""));
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", "") + ".Overlay");
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1 && HideVesselTag.of(stack).hasFluid()) {
            return overlayIcon;
        } else {
            return itemIcon;
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 1) {
            FluidStack fluidStack = HideVesselTag.of(stack).getFluid();
            if (fluidStack != null) {
                return fluidStack.getFluid().getColor(fluidStack);
            }
        }
        return super.getColorFromItemStack(stack, pass);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        FluidStack fluidStack = HideVesselTag.of(stack).getFluid();
        String fluidInfo = fluidStack != null ? " (" + fluidStack.getLocalizedName() + ")" : "";

        return super.getItemStackDisplayName(stack) + fluidInfo;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return getUnlocalizedName() + (HideVesselTag.of(itemstack).hasFluid() ? ".Filled" : ".Empty");
    }

    @Override
    public int getDamage(ItemStack stack) {
        // For recipe matching empty hide vessel has 0 damage
        return HideVesselTag.of(stack).hasFluid() ? 1 : 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        // Actual damage is stored in the NBT
        return HideVesselTag.of(stack).getDamage() / (double)HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return HideVesselTag.of(stack).getDamage() > 0;
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {

        if (ItemHelper.showShiftInformation()) {
            arraylist.add(StatCollector.translateToLocal("gui.Help"));

            if (HideVesselTag.of(is).hasFluid()) {
                arraylist.add(StatCollector.translateToLocal("gui.Help.HideVessel.Filled"));
            } else {
                arraylist.add(StatCollector.translateToLocal("gui.Help.HideVessel.Empty"));
            }
        } else {
            arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

    @Override
    public ItemStack[] loadBagInventory(ItemStack itemStack) {
        return new ItemStack[0];
    }

    @Override
    public FluidStack getFluid(ItemStack itemStack) {
        return HideVesselTag.of(itemStack).getFluid();
    }

    @Override
    public int getCapacity(ItemStack itemStack) {
        return HideVesselHelper.HIDE_VESSEL_CAPACITY;
    }

    @Override
    public int fill(ItemStack container, FluidStack incomingFluid, boolean doFill) {
        if (!HideVesselHelper.isValidHideVesselFluid(incomingFluid.getFluid())) {
            return 0;
        }

        FluidStack currentFluidStack = HideVesselTag.of(container).getFluid();
        if (currentFluidStack != null) {
            int canAcceptAmount = getCapacity(container) - currentFluidStack.amount;
            if (canAcceptAmount > 0 && currentFluidStack.isFluidEqual(incomingFluid)) {
                int amountAccepted = Math.min(canAcceptAmount, incomingFluid.amount);
                if (doFill) {
                    currentFluidStack.amount += amountAccepted;
                    HideVesselTag.of(container)
                        .setFluid(currentFluidStack);
                }

                return amountAccepted;
            }
        } else {
            int amountAccepted = Math.min(getCapacity(container), incomingFluid.amount);
            if (doFill) {
                FluidStack newFluidStack = incomingFluid.copy();
                newFluidStack.amount = amountAccepted;
                HideVesselTag.of(container)
                    .setFluid(newFluidStack);
            }

            return amountAccepted;
        }

        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        FluidStack currentFluidStack = getFluid(container);
        if (currentFluidStack == null) {
            // If someone tries to drain an empty vessel
            // return null and hope they deal with it
            return null;
        }

        FluidStack outgoingFluidStack = currentFluidStack.copy();
        outgoingFluidStack.amount = Math.min(maxDrain, currentFluidStack.amount);

        if (doDrain) {
            if(currentFluidStack.amount - outgoingFluidStack.amount <= 0) {
                HideVesselTag.of(container)
                    .clearFluid();
            } else {
                currentFluidStack.amount -= outgoingFluidStack.amount;
                HideVesselTag.of(container)
                    .setFluid(currentFluidStack);
            }
        }

        return outgoingFluidStack;
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
