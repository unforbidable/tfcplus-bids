package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.AnvilRecipe;
import com.dunk.tfc.api.Crafting.AnvilReq;
import com.dunk.tfc.api.Crafting.PlanRecipe;
import com.dunk.tfc.api.Enums.RuleEnum;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventHandler {

    @SubscribeEvent
    public void onLoadWorld(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {

            // AnvilManager.world needs to have been initialized
            if (AnvilManager.world == null) {
                throw new RuntimeException("AnvilManager not initialized, did we try to add recipes before TFC has?");
            }

            if (AnvilManager.getInstance().getPlan("blowpipe") == null) {
                Bids.LOG.info("Registering blowpipe anvil plan and recipe");
                AnvilManager.getInstance().addPlan("blowpipe", new PlanRecipe(new RuleEnum[] {
                        RuleEnum.BENDLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.ANY }));
                AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronSheet), null,
                        "blowpipe", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.metalBlowpipe, 1, 1)));
                AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.brassSheet), null,
                        "blowpipe", AnvilReq.BRONZE, new ItemStack(BidsItems.brassBlowpipe, 1, 1)));
            }
        }
    }

}
