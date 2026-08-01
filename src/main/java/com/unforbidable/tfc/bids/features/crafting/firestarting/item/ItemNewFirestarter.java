package com.unforbidable.tfc.bids.features.crafting.firestarting.item;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.common.item.ItemCommonTool;
import com.unforbidable.tfc.bids.features.crafting.firestarting.FireStartingConfig;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHelper;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingPlayerState;
import com.unforbidable.tfc.bids.util.playerstate.PlayerStateManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNewFirestarter extends ItemCommonTool {

    protected int maxItemUseDuration = 20;

    public ItemNewFirestarter() {
        super(ToolMaterial.WOOD);
        setCreativeTab(BidsCreativeTabs.bidsTools);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":tools/" +
            getUnlocalizedName().replace("item.", ""));
    }

    public ItemNewFirestarter setMaxItemUseDuration(int maxItemUseDuration) {
        this.maxItemUseDuration = maxItemUseDuration;

        return this;
    }

    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        return Math.round(maxItemUseDuration * FireStartingConfig.fireStartingDurationMultiplier);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (PlayerStateManager.getPlayerState(player, FireStartingPlayerState.class) != null) {
            player.setItemInUse(is, this.getMaxItemUseDuration(is));
        }

        return is;
    }

    @Override
    public boolean onItemUseFirst(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (BidsEventFactory.onFireStartingStart(player, world, x, y, z, side)) {
            FireStartingPlayerState state = new FireStartingPlayerState();
            state.world = world;
            state.x = x;
            state.y = y;
            state.z = z;
            PlayerStateManager.setPlayerState(player, state);
        }

        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        FireStartingHelper.onFireStartingProgress(stack, player.worldObj, player, getMaxItemUseDuration(stack), count);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count) {
        PlayerStateManager.clearPlayerState(player, FireStartingPlayerState.class);
    }

}
