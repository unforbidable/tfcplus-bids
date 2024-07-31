package com.unforbidable.tfc.bids.api.Enums;

public enum EnumWoodFatness {

    INVALID(0),
    POOR(1f),
    GOOD(3f),
    EXCELLENT(4f);

    private final float resinRate;

    EnumWoodFatness(float resinRate) {
        this.resinRate = resinRate;
    }

    public float getResinRate() {
        return resinRate;
    }

    // 0 - "Oak","Aspen","Birch","Chestnut",
    // 4 - "Douglas Fir","Hickory","Maple","Ash",
    // 8 - "Pine","Sequoia","Spruce","Sycamore",
    // 12 - "White Cedar","White Elm","Willow","Kapok",
    // 16 - "Acacia","Palm","Ebony","Fever",
    // 20 - "Baobab","Limba","Mahogany","Teak",
    // 24 - "Bamboo","Gingko","Fruitwood","Mangrove",
    // 28 - "Ghaf","Mahoe","Laurel","Joshua",
    // 32 - "Yew"

    public static EnumWoodFatness fromDamage(int damage) {
        switch (damage) {
            case 17: // Palm (technically not wood)
            case 24: // Bamboo (technically not wood)
            case 31: // Joshua (technically not wood)
                return INVALID;

            case 8: // Pine
                return EXCELLENT;

            case 4: // Douglas Fir
            case 9: // Sequoia
            case 10: // Spruce
            case 12: // White Cedar
                return GOOD;

            default:
                return POOR;
        }
    }

}
