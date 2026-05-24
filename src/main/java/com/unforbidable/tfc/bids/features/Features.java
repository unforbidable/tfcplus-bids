package com.unforbidable.tfc.bids.features;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.features.building.carving.Carving;
import com.unforbidable.tfc.bids.features.building.logwall.LogWall;
import com.unforbidable.tfc.bids.features.building.mudbrick.Mudbrick;
import com.unforbidable.tfc.bids.features.building.roughstone.RoughStone;
import com.unforbidable.tfc.bids.features.building.wattle.Wattle;
import com.unforbidable.tfc.bids.features.crafting.drying.Drying;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.Glassblowing;
import com.unforbidable.tfc.bids.features.device.choppingblock.ChoppingBlock;
import com.unforbidable.tfc.bids.features.device.crucible.Crucible;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRack;
import com.unforbidable.tfc.bids.features.device.firepit.Firepit;
import com.unforbidable.tfc.bids.features.device.woodpile.Woodpile;
import com.unforbidable.tfc.bids.features.material.bark.Bark;
import com.unforbidable.tfc.bids.features.material.birtchback.BirchBark;
import com.unforbidable.tfc.bids.features.material.clothing.Clothing;
import com.unforbidable.tfc.bids.features.material.firewood.Firewood;
import com.unforbidable.tfc.bids.features.material.glass.Glass;
import com.unforbidable.tfc.bids.features.material.logs.Logs;
import com.unforbidable.tfc.bids.features.material.ore.Ore;
import com.unforbidable.tfc.bids.features.material.pottery.Pottery;
import com.unforbidable.tfc.bids.features.resource.quarry.Quarry;
import com.unforbidable.tfc.bids.features.utility.adze.Adze;
import com.unforbidable.tfc.bids.features.utility.compositetools.CompositeTools;
import com.unforbidable.tfc.bids.features.utility.drill.Drill;
import com.unforbidable.tfc.bids.features.utility.fluidcontainers.FluidContainers;
import com.unforbidable.tfc.bids.features.utility.leatherwear.Leatherwear;

public class Features {

    public static final Feature[] features = {
        new Ore(),
        new Crucible(),
        new Pottery(),
        new Mudbrick(),
        new Glassblowing(),
        new Glass(),
        new Drill(),
        new Adze(),
        new Quarry(),
        new Carving(),
        new Woodpile(),
        new Firepit(),
        new RoughStone(),
        new Logs(),
        new LogWall(),
        new Drying(),
        new DryingRack(),
        new Bark(),
        new Firewood(),
        new ChoppingBlock(),
        new Clothing(),
        new BirchBark(),
        new Leatherwear(),
        new Wattle(),
        new FluidContainers(),
        new CompositeTools(),
    };

}
