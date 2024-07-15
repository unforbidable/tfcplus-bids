package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.TileEntities.TEPottery;
import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Firepit.FirepitHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemFirewoodSeasoned extends ItemFirewood implements IFirepitFuelMaterial {

    public ItemFirewoodSeasoned() {
        super();
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !player.isSneaking()) {
            TileEntity te = world.getTileEntity(x, y, z);

            if (te instanceof TEPottery) {
                TEPottery pottery = (TEPottery) te;

                if (pottery.straw == 8 && pottery.wood == 0) {
                    // Because the pottery kiln will eject a wood item
                    // when clicked with a firewood (not a log)
                    // before we get to add another
                    // instead if adding one we add as many as we can
                    // to overcome this issue
                    // Start with the stack of items being held
                    // then walk the inventory to look for more firewood

                    int slot = 0;
                    boolean consumedOtherSlots = false;

                    while (pottery.wood < 8 && slot < player.inventory.getSizeInventory()) {
                        if (itemStack.stackSize > 0) {
                            pottery.addLog(itemStack, player);

                            Bids.LOG.debug("Added firewood from held stack");
                        } else {
                            // Find a slot with a seasoned firewood
                            while (slot < player.inventory.getSizeInventory()
                                    && !(player.inventory.getStackInSlot(slot) != null
                                            && player.inventory.getStackInSlot(slot)
                                                    .getItem() instanceof ItemFirewoodSeasoned
                                            && player.inventory.getStackInSlot(slot).stackSize > 0)) {
                                slot++;
                            }

                            if (slot < player.inventory.getSizeInventory()) {
                                // Found slot with seasoned firewood
                                pottery.addLog(player.inventory.getStackInSlot(slot), player);
                                consumedOtherSlots = true;

                                Bids.LOG.debug("Added firewood from stack in slot: " + slot);
                            }
                        }
                    }

                    if (consumedOtherSlots) {
                        player.inventoryContainer.detectAndSendChanges();
                    }

                    world.markBlockForUpdate(x, y, z);
                } else if (pottery.wood > 0) {
                    // When there are already wood items
                    // in the log pile
                    // eject all instead

                    for (int i = 4; i < 4 + 8; i++) {
                        pottery.ejectItem(i);
                    }

                    pottery.wood = 0;

                    world.markBlockForUpdate(x, y, z);
                }

                return true;
            }
        }

        return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return (int) (FirepitHelper.getEnumFuelMaterial(itemStack).burnTimeMax);
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return (int) (FirepitHelper.getEnumFuelMaterial(itemStack).burnTempMax);
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return FirepitHelper.getEnumFuelMaterial(itemStack);
    }

}
