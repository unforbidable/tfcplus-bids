package com.unforbidable.tfc.bids.Core.Player;

import java.util.HashMap;
import java.util.Map;

import com.unforbidable.tfc.bids.Bids;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerStateManager {

    private static final Map<EntityPlayer, Map<Class<?>, Object>> states = new HashMap<EntityPlayer, Map<Class<?>, Object>>();

    public static <T> void setPlayerState(EntityPlayer player, T state) {
        Class<?> type = state.getClass();
        Map<Class<?>, Object> playerStates;

        if (states.containsKey(player)) {
            playerStates = states.get(player);
        } else {
            playerStates = new HashMap<Class<?>, Object>();
            states.put(player, playerStates);
        }

        Bids.LOG.info("Stored player state: " + state);
        playerStates.put(type, state);
    }

    public static <T> T getPlayerState(EntityPlayer player, Class<T> type) {
        return getPlayerState(player, type, false);
    }

    public static <T> T getPlayerState(EntityPlayer player, Class<T> type, boolean andDoClear) {
        T state = null;

        Map<Class<?>, Object> playerStates = states.get(player);
        if (playerStates != null) {
            Object o = playerStates.get(type);
            if (o != null) {
                state = (T) type.cast(o);
                Bids.LOG.info("Retrieved player state: " + state);

                if (andDoClear) {
                    clearPlayerState(player, type);
                }
            }
        }

        return state;
    }

    public static void clearPlayerState(EntityPlayer player, Class<?> type) {
        Map<Class<?>, Object> playerStates = states.get(player);
        if (playerStates != null) {
            Object o = playerStates.get(type);
            if (o != null) {
                Bids.LOG.info("Cleared player state: " + o);
                playerStates.remove(type);
            }
        }
    }

}
