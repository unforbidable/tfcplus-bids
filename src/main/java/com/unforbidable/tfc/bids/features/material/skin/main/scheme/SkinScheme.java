package com.unforbidable.tfc.bids.features.material.skin.main.scheme;

import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.item.Item;
import java.util.ArrayList;
import java.util.List;

public class SkinScheme {

    public final static List<SkinIndex> skins = new ArrayList<>();

    public static SkinIndex find(Item item) {
        return skins.stream()
            .filter(i -> i.item == item)
            .findFirst().orElse(null);
    }

    public static void setup() {
        skins.add(SkinIndex.builder(BidsItems.genericSkin)
            .build());

        skins.add(SkinIndex.builder(BidsItems.genericFur)
            .fur(SkinFurSpec.generic)
            .build());

        skins.add(SkinIndex.builder(BidsItems.sheepSkin, "sheepTFC")
            .wool(SkinWoolSpec.sheep)
            .build());

        skins.add(SkinIndex.builder(BidsItems.wolfFur, "wolfTFC")
            .fur(SkinFurSpec.wolf)
            .build());

        skins.add(SkinIndex.builder(BidsItems.bearFur, "bearTFC")
            .fur(SkinFurSpec.bear)
            .build());
    }

}
