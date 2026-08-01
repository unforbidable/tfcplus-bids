package com.unforbidable.tfc.bids.features.crafting.firestarting.main.network;

import com.unforbidable.tfc.bids.core.network.packet.PacketConsumable;
import java.util.Random;

public class TinderBurningHandler {

    public static void handlePacket(PacketConsumable<TinderBurningPacket> consumable) {
        double x = consumable.packet.xCoord;
        double y = consumable.packet.yCoord;
        double z = consumable.packet.zCoord;

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double dx = random.nextDouble() * 0.4 - 0.2;
            double dz = random.nextDouble() * 0.4 - 0.2;
            consumable.context.world.spawnParticle("flame", x + dx, y, z + dz, 0.0, 0.0, 0.0);
        }
    }

}
