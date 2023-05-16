package com.unforbidable.tfc.bids.WorldGen;

import codechicken.lib.vec.BlockCoord;
import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.WorldGen.TFCBiome;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.WorldGen.WorldGenHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.*;

public class AquiferWorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        int xCoord = chunkX * 16 + random.nextInt(16) + 8;
        int zCoord = chunkZ * 16 + random.nextInt(16) + 8;
        float rain = TFC_Climate.getRainfall(world, xCoord, Global.SEALEVEL, zCoord);
        if (random.nextInt(getRngForRainfall(rain)) == 0) {
            int size = 1 + Math.min(Math.round(rain / 200), 2) + Math.min(Math.round(rain / 800), 3) * 2 + random.nextInt(2 + Math.min(Math.round(rain / 400), 6));

            if (rain > 55) {
                findSuitableLocationsAndGenerate(random, chunkX, chunkZ, world, size, false);
            } else {
                findSuitableLocationsAndGenerate(random, chunkX, chunkZ, world, size, true);
            }
        }
    }

    private int getRngForRainfall(float rain) {
        if (rain < 55) {
            // More odds for deserts having far less valid blocks
            return 8;
        } else if (rain < 400) {
            // No clay with such rainfall
            return 12;
        } else {
            return 8;
        }
    }

    public boolean findSuitableLocationsAndGenerate(Random random, int chunkX, int chunkZ, World world, int size, boolean desertMode) {
        long start = new Date().getTime();

        int maxY = desertMode ? Global.SEALEVEL : Global.SEALEVEL + 14;

        int invalidBiomeCount = 0;

        int chunkX16 = chunkX * 16;
        int chunkZ16 = chunkZ * 16;

        List<BlockCoord> validBlocks = new ArrayList<BlockCoord>();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int xCoord = chunkX16 + x;
                int zCoord = chunkZ16 + z;

                if (isBiomeValid(world, xCoord, zCoord)) {
                    int y = world.getTopSolidOrLiquidBlock(xCoord, zCoord) - 1;
                    Block topBlock = world.getBlock(xCoord, y, zCoord);
                    if (y >= Global.SEALEVEL - 1 && y <= maxY) {
                        if (isValidAquiferOriginBlock(topBlock)) {
                            validBlocks.add(new BlockCoord(xCoord, y, zCoord));
                        }
                    }
                } else {
                    invalidBiomeCount++;
                }

                if (invalidBiomeCount > 16) {
                    Bids.LOG.debug("Aquifer forced gen observed too many blocks in invalid biomes: " + invalidBiomeCount);

                    long end = new Date().getTime();
                    Bids.LOG.debug("Aquifer forced gen CANCELED due to too many invalid block biomes in: " + (end - start) + "ms");

                    return false;
                }
            }
        }

        if (validBlocks.size() > 0) {
            // Find the most popular height
            Map<Integer, List<BlockCoord>> heightByPopularity = new HashMap<Integer, List<BlockCoord>>();
            for (BlockCoord bc : validBlocks) {
                if (heightByPopularity.containsKey(bc.y)) {
                    heightByPopularity.get(bc.y).add(bc);
                } else {
                    List<BlockCoord> list = new ArrayList<BlockCoord>();
                    list.add(bc);
                    heightByPopularity.put(bc.y, list);
                }
            }

            List<BlockCoord> mostPopularHeightBlocks = null;
            for (List<BlockCoord> list: heightByPopularity.values()) {
                if (mostPopularHeightBlocks == null || mostPopularHeightBlocks.size() < list.size()) {
                    mostPopularHeightBlocks = list;
                }
            }

            if (mostPopularHeightBlocks != null) {
                if (!desertMode && mostPopularHeightBlocks.size() < 16) {
                    long end = new Date().getTime();
                    Bids.LOG.debug("Most popular height is " + mostPopularHeightBlocks.get(0).y + " with " + mostPopularHeightBlocks.size() + " blocks");
                    Bids.LOG.debug("Aquifer gen CANCELLED due to lack of flatness in: " + (end - start) + "ms");
                    return false;
                }

                if (generateAtRandomLocationFromList(random, chunkX, chunkZ, world, size, mostPopularHeightBlocks)) {
                    long end = new Date().getTime();

                    Bids.LOG.debug("Aquifer gen in: " + (end - start) + "ms");

                    return true;
                }
            }
        }

        long end = new Date().getTime();
        Bids.LOG.debug("Aquifer gen FAILED in: " + (end - start) + "ms");

        return false;
    }

    private boolean generateAtRandomLocationFromList(Random random, int chunkX, int chunkZ, World world, int size, List<BlockCoord> locations) {
        Set<Integer> tried = new HashSet<Integer>(locations.size());
        for (int i = 0; i < Math.min(8, locations.size()); i++) {
            int n = random.nextInt(locations.size());
            while (tried.contains(n)) {
                n = random.nextInt(locations.size());
            }

            tried.add(n);
            BlockCoord bc = locations.get(n);

            // Special random seed for the actual gen
            Random random2 = new Random(world.getSeed() + (long) ((chunkX >> 3) - (chunkZ >> 3)) * (chunkZ >> 3) + i);

            boolean success = doGenerateAquifer(random2, world, bc.x, bc.y, bc.z, size);
            if (success) {
                Bids.LOG.debug("Aquifer gen attempt " + (i + 1) + " succeeded");
                return true;
            }

            if (tried.size() == locations.size()) {
                Bids.LOG.debug("Found grass blocks: " + locations.size() + " but no matches, last block was " + bc.x + "," + bc.z);
            }
        }

        return false;
    }

    boolean isValidAquiferOriginBlock(Block block) {
        return TFC_Core.isGrass(block);
    }

    private static boolean isBiomeValid(World world, int xCoord, int zCoord) {
        int biomeID = world.getBiomeGenForCoords(xCoord, zCoord).biomeID;
        return biomeID == TFCBiome.ROLLING_HILLS.biomeID ||
            biomeID == TFCBiome.MOUNTAINS_EDGE.biomeID ||
            biomeID == TFCBiome.MOUNTAINS.biomeID ||
            biomeID == TFCBiome.HIGH_PLAINS.biomeID ||
            biomeID == TFCBiome.HIGH_HILLS.biomeID ||
            biomeID == TFCBiome.HIGH_HILLS_EDGE.biomeID ||
            biomeID == TFCBiome.FOOTHILLS.biomeID ||
            biomeID == TFCBiome.PLAINS.biomeID ||
            biomeID == TFCBiome.SWAMPLAND.biomeID ||
            biomeID == TFCBiome.RIVERBANK.biomeID ||
            biomeID == TFCBiome.LAKESHORE.biomeID ||
            biomeID == TFCBiome.SALTSWAMP.biomeID;
    }

    private boolean isBlockAboveSoil(Block block) {
        return block == Blocks.air ||
            block == TFCBlocks.tallGrass ||
            block == TFCBlocks.worldItem ||
            block == TFCBlocks.flora ||
            block == TFCBlocks.flowers ||
            block == TFCBlocks.flowers2;
    }

    private boolean doGenerateAquifer(Random random, World world, int xCoord, int yCoord, int zCoord, int size) {
        // Find block that isn't soil or gravel below
        int yCoordRawStone = yCoord - 1;
        Block rawStoneBlock = world.getBlock(xCoord, yCoordRawStone, zCoord);

        int n = 10;
        while (TFC_Core.isSoilOrGravel(rawStoneBlock) && n > 0) {
            n--;
            yCoordRawStone--;
            rawStoneBlock = world.getBlock(xCoord, yCoordRawStone, zCoord);

            if (rawStoneBlock == BidsBlocks.aquifer || rawStoneBlock == BidsBlocks.aquifer2) {
                Bids.LOG.debug("Already generated here!");
                return true;
            }
        }

        if (!TFC_Core.isRawStone(rawStoneBlock)) {
            Bids.LOG.debug("Found no raw stone below!");
            return false;
        }

        if (!TFC_Core.isGravel(world.getBlock(xCoord, yCoordRawStone + 1, zCoord))) {
            Bids.LOG.debug("Gravel not found, need to go deeper!");
            for (int i = 0; i < 3; i++) {
                yCoordRawStone--;
                rawStoneBlock = world.getBlock(xCoord, yCoordRawStone, zCoord);

                if (!TFC_Core.isRawStone(rawStoneBlock)) {
                    Bids.LOG.debug("Found not enough raw stone below!");
                    return false;
                }
            }
        }

        Bids.LOG.debug("Randomly move up or down one block!");
        yCoordRawStone += random.nextInt(3) - 1;

        Set<BlockCoord> area = WorldGenHelper.getClusterArea(random, xCoord, yCoord, zCoord, size);
        Set<BlockCoord> border = WorldGenHelper.getBorderOfArea(area);
        Set<BlockCoord> buffer = WorldGenHelper.getBorderOfAreaExcluding(border, area);

        // Check only the buffer area for invalid blocks
        if (!checkBorderAreaIsValid(world, yCoordRawStone, buffer)) {
            Bids.LOG.debug("Invalid buffer!");
            return false;
        }

        Block grassBlock = world.getBlock(xCoord, yCoord, zCoord);
        int grassBlockMetadtata = world.getBlockMetadata(xCoord, yCoord, zCoord);
        BlockGroup blocks = BlockGroup.fromGrass(grassBlock);

        doGenerateBorderArea(random, world, yCoordRawStone, border, blocks, grassBlockMetadtata);
        doGenerateAquiferArea(random, world, yCoordRawStone, area, blocks, grassBlockMetadtata);

        int rodsToGenerate = 1 + Math.min(2, size / 5);
        List<BlockCoord> rodsBlocks = new ArrayList<BlockCoord>(area);
        rodsBlocks.addAll(border);
        if (doGenerateGoldenRodsInArea(random, world, yCoord, rodsBlocks, rodsToGenerate) == 0) {
            // Place a single Golden Rod at the origin
            doGenerateGoldenRodAt(world, xCoord, yCoord, zCoord);
        }

        for (BlockCoord bc: area) {
            world.setBlock(bc.x, yCoord + 3, bc.z, Blocks.glass, 0, 2);
        }

        Bids.LOG.info("Aquifer done at " + xCoord + "," + zCoord + " size: " + size);

        return true;
    }

    private boolean doGenerateGoldenRodAt(World world, int xCoord, int yCoord, int zCoord) {
        int y = world.getTopSolidOrLiquidBlock(xCoord, zCoord);

        // Don't generate rods too high or too low
        if (y > yCoord + 2 || y < yCoord - 2) {
            return false;
        }

        if (TFC_Core.isSoil(world.getBlock(xCoord, y - 1, zCoord)) &&
            isBlockAboveSoil(world.getBlock(xCoord, y, zCoord))) {
            world.setBlock(xCoord, y, zCoord, TFCBlocks.flora, 0, 2);
            Bids.LOG.debug("Placed rod at: " + xCoord + "," + y + "," + zCoord);

            return true;
        }

        return false;
    }

    private int doGenerateGoldenRodsInArea(Random random, World world, int yCoord, List<BlockCoord> list, int count) {
        int actual = 0;
        Set<Integer> tried = new HashSet<Integer>(list.size());
        while (actual < count && tried.size() < list.size()) {
            int n = random.nextInt(list.size());
            while (tried.contains(n)) {
                n = random.nextInt(list.size());
            }

            tried.add(n);
            BlockCoord bc = list.get(n);
            if (doGenerateGoldenRodAt(world, bc.x, yCoord, bc.z)) {
                actual++;
            }
        }

        return actual;
    }

    private boolean checkBorderAreaIsValid(World world, int yCoord, Set<BlockCoord> area) {
        for (int y = - 3; y < 8; y ++) {
            for (BlockCoord bc : area) {
                Block block = world.getBlock(bc.x, yCoord + y, bc.z);
                if (!isValidAquiferBlock(block, y)) {
                    Bids.LOG.debug("Invalid aquifer block: " + block.getUnlocalizedName());
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidAquiferBlock(Block block, int yOffset) {
        if (yOffset > 4) {
            // Anything but water in the top part
            return !TFC_Core.isWater(block) && !TFC_Core.isWaterFlowing(block);
        } else {
            // Solid ground in the bottom part
            return TFC_Core.isDirt(block) ||
                TFC_Core.isGravel(block) ||
                TFC_Core.isGrass(block) ||
                TFC_Core.isClay(block) ||
                TFC_Core.isRawStone(block) ||
                TFC_Core.isClayGrass(block) ||
                TFC_Core.isSand(block);
        }
    }

    private void doGenerateAquiferArea(Random random, World world, int yCoord, Set<BlockCoord> area,
                                       BlockGroup blocks, int metadata) {
        for (BlockCoord bc : area) {
            doGenerateAquiferSingle(random, world, bc.x, yCoord, bc.z, blocks, metadata);
        }
    }

    private void doGenerateAquiferSingle(Random random, World world, int xCoord, int yCoord, int zCoord,
                                         BlockGroup blocks, int metadata) {
        int clayRng = 1 + random.nextInt(20);
        world.setBlock(xCoord, yCoord - 2, zCoord, blocks.aquifer, metadata, 2);
        world.setBlock(xCoord, yCoord - 1, zCoord, blocks.gravel, metadata, 2);
        world.setBlock(xCoord, yCoord, zCoord, clayRng > 7 ? blocks.gravel : blocks.clay, metadata, 2);
        world.setBlock(xCoord, yCoord + 1, zCoord, clayRng > 14 ? blocks.gravel : blocks.clay, metadata, 2);
        for (int i = 0; i < 4; i++) {
            Block previous = world.getBlock(xCoord, yCoord + 1 + i, zCoord);

            if (TFC_Core.isGrassNormal(previous)) {
                world.setBlock(xCoord, yCoord + 1 + i, zCoord, blocks.clayGrass, metadata, 2);
            } else if (i < clayRng / 4 && TFC_Core.isDirt(previous)) {
                world.setBlock(xCoord, yCoord + 1 + i, zCoord, blocks.clay, metadata, 2);
            } else if (TFC_Core.isRawStone(previous)) {
                world.setBlock(xCoord, yCoord + 1 + i, zCoord, blocks.dirt, metadata, 2);
            } else if (i < clayRng / 4 && TFC_Core.isSand(previous)) {
                if (world.isAirBlock(xCoord, yCoord + 1 + i + 1, zCoord)) {
                    world.setBlock(xCoord, yCoord + 1 + i, zCoord, blocks.dryGrass, metadata, 2);
                } else {
                    world.setBlock(xCoord, yCoord + 1 + i, zCoord, blocks.clay, metadata, 2);
                }
            }
        }
    }

    private void doGenerateBorderArea(Random random, World world, int yCoord, Set<BlockCoord> border,
                                      BlockGroup blocks, int metadata) {
        for (BlockCoord bc : border) {
            doGenerateBorderSingle(random, world, bc.x, yCoord, bc.z, blocks, metadata);
        }
    }

    private void doGenerateBorderSingle(Random random, World world, int xCoord, int yCoord, int zCoord,
                                        BlockGroup blocks, int metadata) {
        int clayRng = 1 + random.nextInt(16);
        world.setBlock(xCoord, yCoord - 1, zCoord, blocks.gravel, metadata, 2);
        world.setBlock(xCoord, yCoord, zCoord, blocks.gravel, metadata, 2);
        world.setBlock(xCoord, yCoord + 1, zCoord, blocks.gravel, metadata, 2);
        if (clayRng > 2) {
            if (TFC_Core.isSand(world.getBlock(xCoord, yCoord + 1, zCoord))) {
                world.setBlock(xCoord, yCoord + 1, zCoord, blocks.dirt, metadata, 2);
            } else if (!TFC_Core.isRawStone(world.getBlock(xCoord, yCoord + 1, zCoord))) {
                world.setBlock(xCoord, yCoord + 1, zCoord, blocks.clay, metadata, 2);
            }

            if (clayRng > 12) {
                if (TFC_Core.isSand(world.getBlock(xCoord, yCoord + 2, zCoord))) {
                    world.setBlock(xCoord, yCoord + 2, zCoord, blocks.dirt, metadata, 2);
                } else if (!TFC_Core.isRawStone(world.getBlock(xCoord, yCoord + 2, zCoord))) {
                    world.setBlock(xCoord, yCoord + 2, zCoord, blocks.clay, metadata, 2);
                }
            }
        }
    }

    static class BlockGroup {
        public Block gravel;
        public Block dirt;
        public Block grass;
        public Block dryGrass;
        public Block clay;
        public Block clayGrass;
        public Block aquifer;

        public static BlockGroup fromGrass(Block grass) {
            BlockGroup group = new BlockGroup();

            if (TFC_Core.isGrassType1(grass)) {
                group.gravel = TFCBlocks.gravel;
                group.dirt = TFCBlocks.dirt;
                group.grass = TFCBlocks.grass;
                group.dryGrass = TFCBlocks.dryGrass;
                group.clay = TFCBlocks.clay;
                group.clayGrass = TFCBlocks.clayGrass;
                group.aquifer = BidsBlocks.aquifer;
            } else {
                group.gravel = TFCBlocks.gravel2;
                group.dirt = TFCBlocks.dirt2;
                group.grass = TFCBlocks.grass2;
                group.dryGrass = TFCBlocks.dryGrass2;
                group.clay = TFCBlocks.clay2;
                group.clayGrass = TFCBlocks.clayGrass2;
                group.aquifer = BidsBlocks.aquifer2;
            }

            return group;
        }
    }

}
