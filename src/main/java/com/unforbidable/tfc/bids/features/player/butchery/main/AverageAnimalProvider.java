package com.unforbidable.tfc.bids.features.player.butchery.main;

import com.dunk.tfc.Entities.Mobs.EntityBear;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityDeer;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.Entities.Mobs.EntityHorseTFC;
import com.dunk.tfc.Entities.Mobs.EntityPigTFC;
import com.dunk.tfc.Entities.Mobs.EntitySheepTFC;
import com.dunk.tfc.Entities.Mobs.EntityWolfTFC;
import com.dunk.tfc.api.Entities.IAnimal;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import com.unforbidable.tfc.bids.Bids;
import net.minecraft.world.World;

public class AverageAnimalProvider {

    //[22:50:35] [Client thread/INFO] [TFCPlus Bids]: deer sizeM:0.99966115,sizeF:0.90881425,strM:0.99966115,strF:0.90881425
    //[22:50:40] [Client thread/INFO] [TFCPlus Bids]: goat sizeM:0.99906784,sizeF:0.9136453,strM:0.99906784,strF:0.9136453
    //[22:50:45] [Client thread/INFO] [TFCPlus Bids]: pig sizeM:0.9989218,sizeF:0.8522947,strM:0.9989218,strF:0.8522947
    //[22:50:50] [Client thread/INFO] [TFCPlus Bids]: cow sizeM:0.999585,sizeF:0.90448546,strM:0.999585,strF:0.90448546
    //[22:50:55] [Client thread/INFO] [TFCPlus Bids]: sheep sizeM:0.9989925,sizeF:0.9136818,strM:0.9989925,strF:0.9136818
    //[22:51:00] [Client thread/INFO] [TFCPlus Bids]: wolf sizeM:0.99965286,sizeF:0.9596791,strM:0.99965286,strF:0.9596791
    //[22:51:05] [Client thread/INFO] [TFCPlus Bids]: bear sizeM:0.9964477,sizeF:0.99692464,strM:0.9964477,strF:0.99692464
    //[22:51:12] [Client thread/INFO] [TFCPlus Bids]: horse sizeM:0.9990394,sizeF:0.95790684,strM:0.9990394,strF:0.95790684

    public static float getAverageAnimalSizeAndStrength(IAnimal animal) {
        if (animal instanceof EntityDeer) {
            return ((EntityDeer) animal).getSex() == 0 ? 0.99966115f : 0.90881425f;
        } else if (animal instanceof EntityGoat) {
            return ((EntityGoat) animal).getSex() == 0 ? 0.99906784f : 0.9136453f;
        } else if (animal instanceof EntityPigTFC) {
            return ((EntityPigTFC) animal).getSex() == 0 ? 0.9989218f : 0.8522947f;
        } else if (animal instanceof EntityCowTFC) {
            return ((EntityCowTFC) animal).getSex() == 0 ? 0.999585f : 0.90448546f;
        } else if (animal instanceof EntitySheepTFC) {
            return ((EntitySheepTFC) animal).getSex() == 0 ? 0.9989925f : 0.9136818f;
        } else if (animal instanceof EntityHorseTFC) {
            return ((EntityHorseTFC) animal).getSex() == 0 ? 0.9990394f : 0.95790684f;
        } else if (animal instanceof EntityWolfTFC) {
            return ((EntityWolfTFC) animal).getSex() == 0 ? 0.99965286f : 0.9596791f;
        } else if (animal instanceof EntityBear) {
            // Dimorphism is currently broken for bears, possibly intentional?
            return ((EntityBear) animal).getSex() == 0 ? 0.9964477f : 0.99692464f;
        } else {
            return 1f;
        }
    }

    public static <T extends IAnimal> AverageAnimal test(World world, Class<? extends T> type, Function<T, Integer> sexSupplier) {
        AverageAnimalGenerator<T> generator = new AverageAnimalGenerator<>(world, type);

        int count = 1000000;
        Stream<T> stream = generator.generate(count);

        int countMale = 0;
        int countFemale = 0;
        float sizeModMale = 0;
        float sizeModFemale = 0;
        float strengthModMale = 0;
        float strengthModFemale = 0;

        Iterator<T> it = stream.iterator();
        while (it.hasNext()) {
            T a = it.next();
            if (sexSupplier.apply(a) == 0) {
                countMale++;
                sizeModMale += a.getSizeMod();
                strengthModMale += a.getSizeMod();
            } else {
                countFemale++;
                sizeModFemale += a.getSizeMod();
                strengthModFemale += a.getSizeMod();
            }
        }

        return new AverageAnimal(sizeModMale / countMale, sizeModFemale / countFemale, strengthModMale / countMale, strengthModFemale / countFemale);
    }

    public static void dump(World world) {
        if (world.isRemote) {
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityDeer.class, EntityDeer::getSex);
                Bids.LOG.info("deer sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityGoat.class, EntityGoat::getSex);
                Bids.LOG.info("goat sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityPigTFC.class, EntityPigTFC::getSex);
                Bids.LOG.info("pig sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityCowTFC.class, EntityCowTFC::getSex);
                Bids.LOG.info("cow sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntitySheepTFC.class, EntitySheepTFC::getSex);
                Bids.LOG.info("sheep sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityWolfTFC.class, EntityWolfTFC::getSex);
                Bids.LOG.info("wolf sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityBear.class, EntityBear::getSex);
                Bids.LOG.info("bear sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
            {
                AverageAnimal animal = AverageAnimalProvider.test(world, EntityHorseTFC.class, EntityHorseTFC::getSex);
                Bids.LOG.info("horse sizeM:{},sizeF:{},strM:{},strF:{}", animal.sizeModMale, animal.sizeModFemale, animal.strengthModMale, animal.strengthModFemale);
            }
        }
    }

    static class AverageAnimal {

        public final float sizeModMale;
        public final float sizeModFemale;
        public final float strengthModMale;
        public final float strengthModFemale;

        public AverageAnimal(float sizeModMale, float sizeModFemale, float strengthModMale, float strengthModFemale) {
            this.sizeModMale = sizeModMale;
            this.sizeModFemale = sizeModFemale;
            this.strengthModMale = strengthModMale;
            this.strengthModFemale = strengthModFemale;
        }

    }

    static class AverageAnimalGenerator<T extends IAnimal> {

        private final World world;
        private final Class<? extends T> type;

        public AverageAnimalGenerator(World world, Class<? extends T> type) {
            this.world = world;
            this.type = type;
        }

        public Stream<T> generate(int count) {
            Constructor<T> constructor = getConstructor();
            return Stream.generate(() -> createInstance(constructor))
                .limit(count);
        }

        private T createInstance(Constructor<T> constructor) {
            try {
                return constructor.newInstance(world);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        private Constructor<T> getConstructor() {
            try {
                return (Constructor<T>) type.getConstructor(World.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
