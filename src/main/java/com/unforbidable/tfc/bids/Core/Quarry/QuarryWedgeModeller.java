package com.unforbidable.tfc.bids.Core.Quarry;

import java.util.HashMap;

import net.minecraftforge.common.util.ForgeDirection;

public class QuarryWedgeModeller {

    static HashMap<Integer, QuarryWedgeBounds[]> bounds = new HashMap<Integer, QuarryWedgeBounds[]>();

    public static QuarryWedgeBounds[] getEdgeWedgeBounds(ForgeDirection side, ForgeDirection edge) {
        int hash = edge.ordinal() + side.ordinal() * 6;

        if (!bounds.containsKey(hash)) {
            bounds.put(hash, calculateEdgeWedgeBounds(side, edge));
        }

        return bounds.get(hash);
    }

    private static QuarryWedgeBounds[] calculateEdgeWedgeBounds(ForgeDirection side, ForgeDirection edge) {
        int n = 4;
        QuarryWedgeBounds list[] = new QuarryWedgeBounds[n];

        for (int i = 0; i < n; i++) {
            list[i] = calculateOne(side, edge, i);
        }

        return list;
    }

    private static QuarryWedgeBounds calculateOne(ForgeDirection side, ForgeDirection edge, int i) {
        switch (side) {
            case UP:
                switch (edge) {
                    case NORTH:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 0, 0, /* xyz2 */ wEi(i), wH, wW);

                    case EAST:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wW, 0, wSi(i), /* xyz2 */ 1, wH, wEi(i));

                    case SOUTH:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 0, 1 - wW, /* xyz2 */ wEi(i), wH, 1);

                    case WEST:
                    default:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, 0, wSi(i), /* xyz2 */ wW, wH, wEi(i));
                }

            case SOUTH:
                switch (edge) {
                    case UP:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 1 - wW, 0, /* xyz2 */ wEi(i), 1, wH);

                    case WEST:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, wSi(i), 0, /* xyz2 */ wW, wEi(i), wH);

                    case DOWN:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 0, 0, /* xyz2 */ wEi(i), wW, wH);

                    case EAST:
                    default:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wW, wSi(i), 0, /* xyz2 */ 1, wEi(i), wH);
                }

            case EAST:
                switch (edge) {
                    case UP:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, 1 - wW, wSi(i), /* xyz2 */ wH, 1, wEi(i));

                    case SOUTH:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, wSi(i), 1 - wW, /* xyz2 */ wH, wEi(i), 1);

                    case DOWN:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, 0, wSi(i), /* xyz2 */ wH, wW, wEi(i));

                    case NORTH:
                    default:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, wSi(i), 0, /* xyz2 */ wH, wEi(i), wW);
                }

            case DOWN:
                switch (edge) {
                    case NORTH:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 1 - wH, 0, /* xyz2 */ wEi(i), 1, wW);

                    case EAST:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wW, 1 - wH, wSi(i), /* xyz2 */ 1, 1, wEi(i));

                    case SOUTH:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 1 - wH, 1 - wW, /* xyz2 */ wEi(i), 1, 1);

                    case WEST:
                    default:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, 1 - wH, wSi(i), /* xyz2 */ wW, 1, wEi(i));
                }

            case NORTH:
                switch (edge) {
                    case UP:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 1 - wW, 1 - wH, /* xyz2 */ wEi(i), 1, 1);

                    case WEST:
                        return new QuarryWedgeBounds(/* xyz1 */ 0, wSi(i), 1 - wH, /* xyz2 */ wW, wEi(i), 1);

                    case DOWN:
                        return new QuarryWedgeBounds(/* xyz1 */ wSi(i), 0, 1 - wH, /* xyz2 */ wEi(i), wW, 1);

                    case EAST:
                    default:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wW, wSi(i), 1 - wH, /* xyz2 */ 1, wEi(i), 1);
                }

            case WEST:
                switch (edge) {
                    case UP:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wH, 1 - wW, wSi(i), /* xyz2 */ 1, 1, wEi(i));

                    case SOUTH:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wH, wSi(i), 1 - wW, /* xyz2 */ 1, wEi(i), 1);

                    case DOWN:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wH, 0, wSi(i), /* xyz2 */ 1, wW, wEi(i));

                    case NORTH:
                    default:
                        return new QuarryWedgeBounds(/* xyz1 */ 1 - wH, wSi(i), 0, /* xyz2 */ 1, wEi(i), wW);
                }

            default:
                return new QuarryWedgeBounds(/* xyz1 */ 0, 0, 0, /* xyz2 */ 0, 0, 0);
        }
    }

    static final double u = 1f / 16; // 1 unit
    static final double wS = u * 3; // wedge start
    static final double wD = u * 3; // wedge distance
    static final double wH = u * 2; // wedge height
    static final double wW = u; // wedge width

    // wedge start for i
    static double wSi(int i) {
        return wS + wD * i;
    }

    // wedge end for i
    static double wEi(int i) {
        return wS + wD * i + wW;
    }

}
