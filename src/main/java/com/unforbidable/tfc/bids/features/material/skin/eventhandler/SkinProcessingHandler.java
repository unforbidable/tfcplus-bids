package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingEvent;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceEvent;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.SkinConfig;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.features.material.skin.main.scheme.SkinIndex;
import com.unforbidable.tfc.bids.features.material.skin.main.scheme.SkinScheme;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.item.ItemStack;

public class SkinProcessingHandler {

    private static final Random skillIncreasePartialChance = new Random();

    @SubscribeEvent
    public void onProcessingSurfaceEffortCheck(ProcessingSurfaceEvent.EffortCheck event) {
        if (event.input.getItem() instanceof ItemSkin) {
            SkinTag tag = SkinTag.of(event.input);

            // 1 unit of effort per small skin
            event.newEffort *= (tag.getWeight() / 16);

            // 4 times easier dehairing of skins prepared in lime or lye fluid
            // note: this also reduces BidsStats.materialScraped value but let's just say the value tracks effort for now
            if (tag.isStage(SkinTagAccess.STAGE_PREPARED) && (tag.isFluid(TFCFluids.LIMEWATER.getName()) || tag.isFluid(BidsFluids.weakWoodAshLye.getName()))) {
                event.newEffort *= 0.25f;
            }

            // Effort is further reduced by up to 50% depending on butchering skill
            float skill = TFC_Core.getSkillStats(event.player).getSkillMultiplier(Global.SKILL_BUTCHERING);
            float skillModifier = (1 - skill * 0.5f);
            event.newEffort *= skillModifier;
        }
    }

    @SubscribeEvent
    public void onProcessingItemCrafted(ProcessingEvent.ItemCrafted event) {
        if (event.input.getItem() instanceof ItemSkin && event.result.getItem() instanceof ItemSkin) {
            SkinTag input = SkinTag.of(event.input);
            SkinTag resultTag = SkinTag.of(event.result);

            if (input.hasAnimal()) {
                // Only set the animal if source skin has it
                // otherwise keep result animal if any
                // This is important for dehairing skins of specific animals into generic dehaired skins
                resultTag.setAnimal(input.getAnimal());
            }

            // Salted is removed with the scraped material
            resultTag.setSalted(false);

            resultTag.setFluid(input.getFluid());
            resultTag.setWeight(input.getWeight());

            // Some decay is removed after fleshing and dehairing
            // decay less than 1% is forgiven
            float decayMultiplier = getDecayMultiplierForStage(resultTag.getStage());
            if (decayMultiplier != 1 && input.getDecay() > 0) {
                float newDecay = Math.max(0, input.getDecay()) * decayMultiplier;

                if (newDecay / input.getWeight() < 0.01f) {
                    resultTag.setDecay(0);
                } else {
                    resultTag.setDecay(Math.round(newDecay * 100) / 100f);
                }

                resultTag.setDecayTimer(input.getDecayTimer() + 6);
            } else {
                resultTag.setDecay(input.getDecay());
                resultTag.setDecayTimer(input.getDecayTimer());
            }

            float skillIncreasePerStage = getSkillGainForStage(resultTag.getStage());
            if (skillIncreasePerStage > 0) {
                float skillIncreaseFloat = skillIncreasePerStage * resultTag.getWeight() / SkinHelper.WEIGHT_SMALL;
                int skillIncreaseInt = (int) Math.floor(skillIncreaseFloat);
                int skillIncrease = skillIncreaseInt + (skillIncreasePartialChance.nextFloat() < (skillIncreaseFloat - skillIncreaseInt) ? 1 : 0);
                TFC_Core.getSkillStats(event.player).increaseSkill(Global.SKILL_BUTCHERING, skillIncrease);
            }

            SkinIndex skin = SkinScheme.find(event.input.getItem());
            if (skin != null && skin.wool != null) {
                // Sheepskin shearing, must be Clean or Preserved
                if (input.isStage(SkinTagAccess.STAGE_CLEAN) || input.isStage(SkinTagAccess.STAGE_PRESERVED)) {
                    // Stack size depends on weight, 1 per small skin size
                    // which can be 0 if the skin is too small
                    float weight = input.getWeight();
                    int stackSize = (int) Math.floor(weight / SkinHelper.WEIGHT_SMALL);
                    if (stackSize > 0) {
                        ItemStack wool = skin.wool.extraDrop.copy();
                        wool.stackSize *= stackSize;
                        TFC_Core.giveItemToPlayer(wool, event.player);
                    }
                }
            }
        }
    }

    private float getSkillGainForStage(String stage) {
        switch (stage) {
            case SkinTagAccess.STAGE_FLESHED:
                return SkinConfig.butcheringSkillGainPerSmallSkinFleshed;
            case SkinTagAccess.STAGE_DEHAIRED:
                return SkinConfig.butcheringSkillGainPerSmallSkinDehaired;
            case SkinTagAccess.STAGE_WORKED:
                return SkinConfig.butcheringSkillGainPerSmallSkinWorked;
            default:
                return 0;
        }
    }

    private float getDecayMultiplierForStage(String stage) {
        if (stage.equals(SkinTagAccess.STAGE_FLESHED)) {
            return 0.25f;
        } else if (stage.equals(SkinTagAccess.STAGE_DEHAIRED)) {
            return 0.5f;
        } else {
            return 1f;
        }
    }

}
