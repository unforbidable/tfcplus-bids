package com.unforbidable.tfc.bids.Core.Wood;

public class WoodHelper {

    // 0 - "Oak","Aspen","Birch","Chestnut",
    // 4 - "Douglas Fir","Hickory","Maple","Ash",
    // 8 - "Pine","Sequoia","Spruce","Sycamore",
    // 12 - "White Cedar","White Elm","Willow","Kapok",
    // 16 - "Acacia","Palm","Ebony","Fever",
    // 20 - "Baobab","Limba","Mahogany","Teak",
    // 24 - "Bamboo","Gingko","Fruitwood","Mangrove",
    // 28 - "Ghaf","Mahoe","Laurel","Joshua",
    // 32 - "Yew"

    public static boolean canPeelLog(int meta) {
        if (meta == 24) {
            return false;
        }

        return true;
    }

}
