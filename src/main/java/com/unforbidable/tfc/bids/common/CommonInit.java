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
        EnumHelper.addToolMaterial(ToolMaterialNames.HARDENED_WOOD, 0, 80, 5.0f, 100, 1);
        EnumHelper.addToolMaterial(ToolMaterialNames.FLINT, 0, 250, 9.0f, 250, 1);
        EnumHelper.addToolMaterial(ToolMaterialNames.ANTLER, 0, 300, 6.0f, 150, 1);
    }

}
