package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Entities.EntityProjectileTFC;
import com.dunk.tfc.Entities.Mobs.EntityBear;
import com.dunk.tfc.Entities.Mobs.EntityBighornSheepTFC;
import com.dunk.tfc.Entities.Mobs.EntityBison;
import com.dunk.tfc.Entities.Mobs.EntityBuffalo;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityDeer;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.Entities.Mobs.EntityHorseTFC;
import com.dunk.tfc.Entities.Mobs.EntityPeccaryTFC;
import com.dunk.tfc.Entities.Mobs.EntityPigTFC;
import com.dunk.tfc.Entities.Mobs.EntitySheepTFC;
import com.dunk.tfc.Entities.Mobs.EntityWarthogTFC;
import com.dunk.tfc.Entities.Mobs.EntityWolfTFC;
import com.dunk.tfc.Items.ItemLeather;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Entities.IAnimal;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.material.skin.SkinConfig;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import java.util.ArrayList;

public class SkinLivingDropsEventHandler {

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (SkinConfig.enableAnimalSkinDropReplacement) {
            if (event.entityLiving instanceof IAnimal) {
                IAnimal animal = (IAnimal) event.entityLiving;
                float ageMod = TFC_Core.getPercentGrown(animal);
                float sizeMod = animal.getSizeMod();

                EntityPlayer butcherPlayer = getButcherPlayer(event);
                float butcherMod = butcherPlayer != null ? getButcherSkillBonus(butcherPlayer) : 0;

                SkinResult result = getFreshAnimalSkin(animal, ageMod, sizeMod);
                if (result != null) {
                    float bonus = SkinHelper.SKIN_MIN_SIZE_BONUS + (SkinHelper.SKIN_MAX_SIZE_BONUS - SkinHelper.SKIN_MIN_SIZE_BONUS) * butcherMod;
                    float weight = Math.round(Math.pow(2, result.size) * SkinHelper.WEIGHT_SMALL * bonus);

                    Bids.LOG.info("Dropped skin size: {} (size: {}, butcher: {})", weight, result.size, bonus);

                    ItemStack is = SkinHelper.createStack(result.item, weight, tag -> tag.setAnimal(result.name));
                    EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, is);

                    purgeOriginalDrops(event.drops);

                    event.drops.add(entityItem);
                }
            }
        }
    }

    private void purgeOriginalDrops(ArrayList<EntityItem> drops) {
        for (int i = 0; i < drops.size(); i++) {
            ItemStack is = drops.get(i).getEntityItem();
            if (is.getItem() instanceof ItemLeather) {
                Bids.LOG.info("Dropped original size: {}", is.getItemDamage());

                if (is.getItem() == TFCItems.wolfFur) {
                    drops.get(i).setEntityItemStack(new ItemStack(TFCItems.wolfFurHat));
                } else if (is.getItem() == TFCItems.bearFur) {
                    drops.get(i).setEntityItemStack(new ItemStack(TFCItems.bearFurHat));
                } else {
                    drops.remove(i--);
                }
            }
        }
    }

    static class SkinResult {
        public final Item item;
        public final float size;
        public final String name;

        public SkinResult(Item item, float size, String name) {
            this.item = item;
            this.size = size;
            this.name = name;
        }
    }

    private SkinResult getFreshAnimalSkin(IAnimal animal, float ageMod, float sizeMod) {
        if (animal instanceof EntitySheepTFC) {
            if (animal.isDomesticated()) {
                if (((EntitySheepTFC) animal).getSheared()) {
                    // sheepskin: ageMod * sizeMod
                    return new SkinResult(BidsItems.sheepSkin, ageMod * sizeMod, null);
                } else {
                    // hide: ageMod * sizeMod
                    return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod, "sheepTFC");
                }
            } else {
                if (animal instanceof EntityBighornSheepTFC) {
                    // fur: ageMod * sizeMod * 1.4
                    return new SkinResult(BidsItems.genericFur, ageMod * sizeMod * 1.4f, "bighornsheepTFC");
                } else {
                    // fur: ageMod * sizeMod * 1.1
                    return new SkinResult(BidsItems.genericFur, ageMod * sizeMod * 1.1f, "mouflonTFC");
                }
            }
        } else if (animal instanceof EntityCowTFC) {
            // hide: ageMod * 3 - 1
            if (animal.isDomesticated()) {
                return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "cowTFC");
            } else {
                if (animal instanceof EntityBison) {
                    return new SkinResult(BidsItems.genericFur, ageMod * sizeMod * 3 - 1, "bisonTFC");
                } else if (animal instanceof EntityBuffalo) {
                    return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "buffaloTFC");
                } else {
                    return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "aurochsTFC");
                }
            }
        } else if (animal instanceof EntityDeer) {
            // fur: ageMod * sizeMod * 1.84
            return new SkinResult(BidsItems.genericFur, ageMod * sizeMod * 1.4f, "deerTFC");
        } else if (animal instanceof EntityBear) {
            // bear fur: ageMod * 3 - 1
            return new SkinResult(BidsItems.bearFur, ageMod * sizeMod * 3 - 1, null);
        } else if (animal instanceof EntityWolfTFC) {
            // wolf fur: sizeMod * ageMod * 1.1
            return new SkinResult(BidsItems.wolfFur, ageMod * sizeMod * 1.1f, null);
        } else if (animal instanceof EntityGoat) {
            if (animal.isDomesticated()) {
                // hide: ageMod * sizeMod
                return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 1.1f, "goatTFC");
            } else {
                // fur: ageMod * sizeMod * 1.1
                return new SkinResult(BidsItems.genericFur, ageMod * sizeMod * 1.1f, "ibexTFC");
            }
        } else if (animal instanceof EntityHorseTFC) {
            // hide: ageMod * 3 - 1
            if (animal.isDomesticated()) {
                switch (((EntityHorseTFC) animal).getHorseType()) {
                    case 0:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "horseTFC");
                    case 1:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "donkeyTFC");
                    case 2:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "muleTFC");
                }
            } else {
                switch (((EntityHorseTFC) animal).getHorseType()) {
                    case 0:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "wildHorseTFC");
                    case 1:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "wildAssTFC");
                    case 2:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "muleTFC");
                    case 3:
                        return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod * 3 - 1, "zebraTFC");
                }
            }
        } else if (animal instanceof EntityPigTFC) {
            // hide: ageMod * sizeMod
            if (animal.isDomesticated()) {
                return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod, "pigTFC");
            } else {
                if (animal instanceof EntityPeccaryTFC) {
                    return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod, "peccaryTFC");
                } else if (animal instanceof EntityWarthogTFC) {
                    return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod, "warthogTFC");
                } else {
                    return new SkinResult(BidsItems.genericSkin, ageMod * sizeMod, "boarTFC");
                }
            }
        }

        return null;
    }

    private float getButcherSkillBonus(EntityPlayer player) {
        float skill = TFC_Core.getSkillStats(player).getSkillMultiplier(Global.SKILL_BUTCHERING);
        return Math.max(0, Math.min(1, skill));
    }

    private static EntityPlayer getButcherPlayer(LivingDropsEvent event) {
        if (event.source.getSourceOfDamage() instanceof EntityPlayer) {
            return (EntityPlayer) event.source.getSourceOfDamage();
        } else if (event.source.getSourceOfDamage() instanceof EntityProjectileTFC) {
            EntityProjectileTFC proj = (EntityProjectileTFC) event.source.getSourceOfDamage();
            if (proj.shootingEntity instanceof EntityPlayer) {
                return (EntityPlayer) proj.shootingEntity;
            }
        }

        return null;
    }

}
