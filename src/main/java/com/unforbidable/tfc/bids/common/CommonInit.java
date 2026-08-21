package com.unforbidable.tfc.bids.common;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.names.ToolMaterialNames;
import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.util.EnumHelper;

public class CommonInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Bids.LOG.info("Inject tool material");
        EnumHelper.addToolMaterial(ToolMaterialNames.HARDENED_WOOD, 0, 60, 5.0f, 100, 1);
    }

}
