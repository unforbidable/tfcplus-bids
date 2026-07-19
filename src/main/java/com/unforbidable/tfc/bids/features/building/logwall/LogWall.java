package com.unforbidable.tfc.bids.features.building.logwall;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.building.carving.CarvingRegistry;
import com.unforbidable.tfc.bids.features.building.logwall.block.BlockLogWall;
import com.unforbidable.tfc.bids.features.building.logwall.block.BlockLogWallVert;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWall;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWall16;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWall32;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWallVert;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWallVert16;
import com.unforbidable.tfc.bids.features.building.logwall.block.blockitem.ItemLogWallVert32;
import com.unforbidable.tfc.bids.features.building.logwall.main.LogWallType;
import com.unforbidable.tfc.bids.features.building.logwall.main.LogWallVertType;
import com.unforbidable.tfc.bids.features.building.logwall.main.carvable.CarvableLogWall;
import com.unforbidable.tfc.bids.features.building.logwall.main.carvable.CarvableLogWallVert;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_CORNER;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_CORNER_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_CORNER_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_CORNER_ALT;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_CORNER_ALT_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_CORNER_ALT_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_EAST;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_EAST_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_EAST_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_EAST_ALT;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_EAST_ALT_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_EAST_ALT_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_NORTH;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_NORTH_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_NORTH_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_NORTH_ALT;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_NORTH_ALT_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_NORTH_ALT_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_VERT;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_VERT_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_VERT_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_VERT_ALT;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_VERT_ALT_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LOG_WALL_VERT_ALT_3;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("logWall")
public class LogWall extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(LOG_WALL_EAST, () -> new BlockLogWall(LogWallType.EAST, 0), ItemLogWall.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_NORTH, () -> new BlockLogWall(LogWallType.NORTH, 0), ItemLogWall.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_CORNER, () -> new BlockLogWall(LogWallType.CORNER, 0), ItemLogWall.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_EAST_ALT, () -> new BlockLogWall(LogWallType.EAST_ALT, 0), ItemLogWall.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_NORTH_ALT, () -> new BlockLogWall(LogWallType.NORTH_ALT, 0), ItemLogWall.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_CORNER_ALT, () -> new BlockLogWall(LogWallType.CORNER_ALT, 0), ItemLogWall.class)
            .fireInfo(5, 5);

        init.block(LOG_WALL_EAST_2, () -> new BlockLogWall(LogWallType.EAST, 16), ItemLogWall16.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_NORTH_2, () -> new BlockLogWall(LogWallType.NORTH, 16), ItemLogWall16.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_CORNER_2, () -> new BlockLogWall(LogWallType.CORNER, 16), ItemLogWall16.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_EAST_ALT_2, () -> new BlockLogWall(LogWallType.EAST_ALT, 16), ItemLogWall16.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_NORTH_ALT_2, () -> new BlockLogWall(LogWallType.NORTH_ALT, 16), ItemLogWall16.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_CORNER_ALT_2, () -> new BlockLogWall(LogWallType.CORNER_ALT, 16), ItemLogWall16.class)
            .fireInfo(5, 5);

        init.block(LOG_WALL_EAST_3, () -> new BlockLogWall(LogWallType.EAST, 32), ItemLogWall32.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_NORTH_3, () -> new BlockLogWall(LogWallType.NORTH, 32), ItemLogWall32.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_CORNER_3, () -> new BlockLogWall(LogWallType.CORNER, 32), ItemLogWall32.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_EAST_ALT_3, () -> new BlockLogWall(LogWallType.EAST_ALT, 32), ItemLogWall32.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_NORTH_ALT_3, () -> new BlockLogWall(LogWallType.NORTH_ALT, 32), ItemLogWall32.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_CORNER_ALT_3, () -> new BlockLogWall(LogWallType.CORNER_ALT, 32), ItemLogWall32.class)
            .fireInfo(5, 5);

        init.block(LOG_WALL_VERT, () -> new BlockLogWallVert(LogWallVertType.DEFAULT, 0), ItemLogWallVert.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_VERT_ALT, () -> new BlockLogWallVert(LogWallVertType.ALT, 0), ItemLogWallVert.class)
            .fireInfo(5, 5);

        init.block(LOG_WALL_VERT_2, () -> new BlockLogWallVert(LogWallVertType.DEFAULT, 16), ItemLogWallVert16.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_VERT_ALT_2, () -> new BlockLogWallVert(LogWallVertType.ALT, 16), ItemLogWallVert16.class)
            .fireInfo(5, 5);

        init.block(LOG_WALL_VERT_3, () -> new BlockLogWallVert(LogWallVertType.DEFAULT, 32), ItemLogWallVert32.class)
            .fireInfo(5, 5);
        init.block(LOG_WALL_VERT_ALT_3, () -> new BlockLogWallVert(LogWallVertType.ALT, 32), ItemLogWallVert32.class)
            .fireInfo(5, 5);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .hide(BidsBlocks.logWallEastAlt)
            .hide(BidsBlocks.logWallEastAlt2)
            .hide(BidsBlocks.logWallEastAlt3)
            .hide(BidsBlocks.logWallNorth)
            .hide(BidsBlocks.logWallNorth2)
            .hide(BidsBlocks.logWallNorth3)
            .hide(BidsBlocks.logWallNorthAlt)
            .hide(BidsBlocks.logWallNorthAlt2)
            .hide(BidsBlocks.logWallNorthAlt3)
            .hide(BidsBlocks.logWallCorner)
            .hide(BidsBlocks.logWallCorner2)
            .hide(BidsBlocks.logWallCorner3)
            .hide(BidsBlocks.logWallCornerAlt)
            .hide(BidsBlocks.logWallCornerAlt2)
            .hide(BidsBlocks.logWallCornerAlt3)
            .hide(BidsBlocks.logWallVertAlt)
            .hide(BidsBlocks.logWallVertAlt2)
            .hide(BidsBlocks.logWallVertAlt3);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasLogWall()) {
                setup.ores("blockLogWall")
                    .add(wood.blocks.getLogWall())
                    .add(wood.blocks.getLogWallVert());
            }

            if (wood.blocks.hasLogWall()) {
                if (wood.items.hasSeasonedPeeledLog()) {
                    setup.recipes().addShaped(wood.blocks.getLogWall(),
                            "A ", "11", '1', wood.getOreWithSuffix("logWoodSeasoned"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    setup.recipes().addShapeless(wood.items.getSeasonedPeeledLog(2),
                        wood.blocks.getLogWall());

                    setup.recipes().addShaped(wood.blocks.getLogWallVert(),
                            "A1", " 1", '1', wood.getOreWithSuffix("logWoodSeasoned"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    setup.recipes().addShapeless(wood.items.getSeasonedPeeledLog(2),
                        wood.blocks.getLogWallVert());
                } else {
                    setup.recipes().addShaped(wood.blocks.getLogWall(),
                            "A ", "11", '1', wood.getOreWithSuffix("logWood"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    setup.recipes().addShapeless(wood.items.getLog(2),
                        wood.blocks.getLogWall());

                    setup.recipes().addShaped(wood.blocks.getLogWallVert(),
                            "A1", " 1", '1', wood.getOreWithSuffix("logWood"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    setup.recipes().addShapeless(wood.items.getLog(2),
                        wood.blocks.getLogWallVert());
                }
            }
        }

        setup.registry(CarvingRegistry.carvable)
            .add(new CarvableLogWall())
            .add(new CarvableLogWallVert());
    }

}
