package com.unforbidable.tfc.bids.api.Enums;

public enum EnumWoodHardness {
    INVALID,
    SOFT,
    MODERATE,
    HARD;

    // 0 - "Oak","Aspen","Birch","Chestnut",
    // 4 - "Douglas Fir","Hickory","Maple","Ash",
    // 8 - "Pine","Sequoia","Spruce","Sycamore",
    // 12 - "White Cedar","White Elm","Willow","Kapok",
    // 16 - "Acacia","Palm","Ebony","Fever",
    // 20 - "Baobab","Limba","Mahogany","Teak",
    // 24 - "Bamboo","Gingko","Fruitwood","Mangrove",
    // 28 - "Ghaf","Mahoe","Laurel","Joshua",
    // 32 - "Yew"

    public static EnumWoodHardness fromDamage(int damage) {
        switch (damage) {
            case 17: // Palm (technically not wood)
            case 24: // Bamboo (technically not wood)
            case 31: // Joshua (technically not wood)
                return INVALID;

            case 4: // Douglas Fir
            case 8: // Pine
            case 9: // Sequoia
            case 10: // Spruce
            case 12: // White Cedar
            case 20: // Baobab (soft hardwood)
            case 25: // Gingko (soft internal structure)
                return SOFT;

            case 11: // Sycamore
            case 19: // Fever
                return MODERATE;

            // case 32: // Yew (hard softwood)
            default:
                return HARD;
        }
    }

}
