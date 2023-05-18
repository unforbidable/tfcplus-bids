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
            Random random2 = new Random(random.nextLong());

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
            biomeID == TFCBiome.MOUNTAIN_RANGE_EDGE.biomeID ||
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

    private boolean isBlockReplacableAt(Block block) {
        return block == Blocks.air ||
            block == TFCBlocks.tallGrass ||
            block == TFCBlocks.worldItem ||
            block == TFCBlocks.flora ||
            block == TFCBlocks.flowers ||
            block == TFCBlocks.flowers2;
    }

    private boolean doGenerateAquifer(Random random, World world, int xCoord, int yCoord, int zCoord, int size) {
        int yCoordAquifer = yCoord - 1;

        for (int i = 0; i < 10; i++) {
            Block rawStoneBlock = world.getBlock(xCoord, yCoordAquifer, zCoord);

            if (!TFC_Core.isSoilOrGravel(rawStoneBlock) && !TFC_Core.isRawStone(rawStoneBlock)) {
                Bids.LOG.debug("Found no raw stone below!");
                return false;
            }

            if (rawStoneBlock == BidsBlocks.aquifer || rawStoneBlock == BidsBlocks.aquifer2) {
                Bids.LOG.debug("Already generated here!");
                return true;
            }

            yCoordAquifer--;
        }

        // Randomly move
        yCoordAquifer += 2 + random.nextInt(2);

        Set<BlockCoord> area = WorldGenHelper.getClusterArea(random, xCoord, yCoord, zCoord, size);
        Set<BlockCoord> border = WorldGenHelper.getBorderOfArea(area);
        Set<BlockCoord> buffer = WorldGenHelper.getBorderOfAreaExcluding(border, area);

        // Check only the buffer area for invalid blocks
        if (!checkBorderAreaIsValid(world, yCoordAquifer, buffer)) {
            Bids.LOG.debug("Invalid buffer!");
            return false;
        }

        Block grassBlock = world.getBlock(xCoord, yCoord, zCoord);
        int grassBlockMetadtata = world.getBlockMetadata(xCoord, yCoord, zCoord);
        BlockGroup blocks = BlockGroup.fromGrass(grassBlock);

        doGenerateAquiferArea(random, world, yCoordAquifer, area, border, blocks, grassBlockMetadtata);

        List<BlockCoord> areaAndBorderBlocks = new ArrayList<BlockCoord>(area);
        areaAndBorderBlocks.addAll(border);

        int rodsToGenerate = 1 + Math.min(2, size / 5);
        if (!doGenerateGoldenRodsInArea(random, world, yCoord, areaAndBorderBlocks, rodsToGenerate)) {
            // Place a single Golden Rod at the origin
            doGenerateGoldenRodAt(world, xCoord, yCoord + 1, zCoord);
            world.setBlock(xCoord, yCoord + 4, zCoord, Blocks.glass, 0, 2);
        }

        for (BlockCoord bc: area) {
            world.setBlock(bc.x, yCoord + 3, bc.z, Blocks.glass, 0, 2);
        }

        Bids.LOG.debug("Aquifer done at " + xCoord + "," + zCoord + " size: " + size + " depth: " + (yCoord - yCoordAquifer));

        return true;
    }

    private void doGenerateGoldenRodAt(World world, int xCoord, int yCoord, int zCoord) {
        world.setBlock(xCoord, yCoord, zCoord, TFCBlocks.flora, 0, 2);
        Bids.LOG.debug("Placed rod at: " + xCoord + "," + yCoord + "," + zCoord);
    }

    private boolean doGenerateGoldenRodsInArea(Random random, World world, int yCoord, List<BlockCoord> list, int count) {
        List<BlockCoord> clayList = new ArrayList<BlockCoord>();
        List<BlockCoord> dirtList = new ArrayList<BlockCoord>();
        for (BlockCoord bc : list) {
            int y = world.getTopSolidOrLiquidBlock(bc.x, bc.z);
            if (y < yCoord + 3 && y > yCoord - 3 && isBlockReplacableAt(world.getBlock(bc.x, y, bc.z))) {
                Block b = world.getBlock(bc.x, y - 1, bc.z);
                if (TFC_Core.isClayGrass(b)) {
                    clayList.add(new BlockCoord(bc.x, y, bc.z));
                } else if (TFC_Core.isGrass(b)) {
                    dirtList.add(new BlockCoord(bc.x, y, bc.z));
                }
            }
        }

        int actual = doGenerateGoldenRodsAtSpecificBlocks(random, world, clayList, count);

        if (actual < count) {
            actual += doGenerateGoldenRodsAtSpecificBlocks(random, world, dirtList, count - actual);
        }

        Bids.LOG.debug("Placed golden rods " + actual + "/" + count);

        return actual != 0;
    }

    private int doGenerateGoldenRodsAtSpecificBlocks(Random random, World world, List<BlockCoord> list, int count) {
        Set<Integer> tried = new HashSet<Integer>(list.size());

        while (tried.size() < list.size() && tried.size() < count) {
            int n = random.nextInt(list.size());
            while (tried.contains(n)) {
                n = random.nextInt(list.size());
            }

            tried.add(n);
            BlockCoord bc = list.get(n);
            doGenerateGoldenRodAt(world, bc.x, bc.y, bc.z);
        }

        return tried.size();
    }

    private boolean checkBorderAreaIsValid(World world, int yCoord, Set<BlockCoord> area) {
        for (int y = - 1; y < 10; y ++) {
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

    private void doGenerateAquiferArea(Random random, World world, int yCoord, Set<BlockCoord> area, Set<BlockCoord> border,
                                       BlockGroup blocks, int metadata) {
        for (BlockCoord bc : area) {
            doGenerateAquiferSingle(random, world, bc.x, yCoord, bc.z, blocks, metadata, false);
        }
        for (BlockCoord bc : border) {
            doGenerateAquiferSingle(random, world, bc.x, yCoord, bc.z, blocks, metadata, true);
        }
    }

    private void doGenerateAquiferSingle(Random random, World world, int xCoord, int yCoord, int zCoord,
                                         BlockGroup blocks, int metadata, boolean border) {
        if (!border) {
            world.setBlock(xCoord, yCoord, zCoord, blocks.aquifer, metadata, 2);
        }

        int clayRng = (border ? 1 : 5) + random.nextInt(20);

        world.setBlock(xCoord, yCoord + 1, zCoord, blocks.gravel, metadata, 2);
        world.setBlock(xCoord, yCoord + 2, zCoord, clayRng > 19 ? blocks.clay : blocks.gravel, metadata, 2);
        world.setBlock(xCoord, yCoord + 3, zCoord, clayRng > 14 ? blocks.clay : blocks.gravel, metadata, 2);
        world.setBlock(xCoord, yCoord + 4, zCoord, clayRng > 9 ? blocks.clay : blocks.gravel, metadata, 2);

        for (int i = 0; i < 6; i++) {
            int y = yCoord + 4 + i;
            Block previous = world.getBlock(xCoord, y, zCoord);

            if (TFC_Core.isSand(previous)) {
                if (i < clayRng / 3 || random.nextInt(3) != 0) {
                    if (world.isAirBlock(xCoord, y + 1, zCoord)) {
                        world.setBlock(xCoord, y, zCoord, blocks.dryGrass, metadata, 2);
                    } else {
                        world.setBlock(xCoord, y, zCoord, blocks.dirt, metadata, 2);
                    }
                }
            } else {
                if (i < clayRng / 3 && random.nextInt(3) != 0) {
                    if (TFC_Core.isGrassNormal(previous)) {
                        world.setBlock(xCoord, y, zCoord, blocks.clayGrass, metadata, 2);
                    } else if (TFC_Core.isDirt(previous) || TFC_Core.isRawStone(previous)) {
                        world.setBlock(xCoord, y, zCoord, blocks.clay, metadata, 2);
                    }
                } else {
                    if (TFC_Core.isRawStone(previous)) {
                        world.setBlock(xCoord, y, zCoord, blocks.dirt, metadata, 2);
                    }
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
