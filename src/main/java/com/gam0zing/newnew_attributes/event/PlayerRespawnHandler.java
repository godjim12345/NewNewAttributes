package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.NNAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerRespawnHandler {
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        setHealth(player);
        setFood(player);
        setSaturation(player);

    }

    private void setHealth(Player player) {

        if (player.getAttribute(NNAttributes.RESPAWN_HEALTH_RATE.get()) == null
        || Double.isNaN(player.getAttribute(NNAttributes.RESPAWN_HEALTH_RATE.get()).getValue()))
            return;

        player.setHealth((float) Math.min(
                player.getHealth() * Math.max(
                        1 + player.getAttribute(NNAttributes.RESPAWN_HEALTH_RATE.get()).getValue(),
                        0.01),
                1));
    }

    private void setFood(Player player) {

        if (player.getAttribute(NNAttributes.RESPAWN_FOOD_RATE.get()) == null
                || Double.isNaN(player.getAttribute(NNAttributes.RESPAWN_FOOD_RATE.get()).getValue()))
            return;

        FoodData foodData = player.getFoodData();
        foodData.setFoodLevel((int) Math.min(
                foodData.getFoodLevel() * Math.max(
                        1 + player.getAttribute(NNAttributes.RESPAWN_HEALTH_RATE.get()).getValue(),
                        0),
                1));
    }

    private void setSaturation(Player player) {

        if (player.getAttribute(NNAttributes.RESPAWN_SATURATION_RATE.get()) == null
                || Double.isNaN(player.getAttribute(NNAttributes.RESPAWN_SATURATION_RATE.get()).getValue()))
            return;

        FoodData foodData = player.getFoodData();
        foodData.setSaturation((float) Math.min(
                foodData.getSaturationLevel() * Math.max(
                        1 + player.getAttribute(NNAttributes.RESPAWN_SATURATION_RATE.get()).getValue(),
                        0),
                1));
    }

    /*private <T> void setStatus(Player player, T type) {

        if (player.getAttribute(NNAttributes.RESPAWN_FOOD_RATE.get()) == null
                || Double.isNaN(player.getAttribute(NNAttributes.RESPAWN_FOOD_RATE.get()).getValue()))
            return;

        FoodData foodData = player.getFoodData();
        foodData.setFoodLevel((int) Math.min(
                20 * Math.max(
                        1 + player.getAttribute(NNAttributes.RESPAWN_HEALTH_RATE.get()).getValue(),
                        0),
                1));
    }*/
}