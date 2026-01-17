package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.ModAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerRespawn {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        setHealth(player);
        setFood(player);
        setSaturation(player);

    }

    //玩家克隆事件，运行于死亡后，重生前
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.isWasDeath()) { // 只有在死亡重生时才执行
            Player originalPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();

            // 复制属性值到新玩家
            copyAttribute(originalPlayer, newPlayer, ModAttributes.RESPAWN_HEALTH_RATE.get());
            copyAttribute(originalPlayer, newPlayer, ModAttributes.RESPAWN_FOOD_RATE.get());
            copyAttribute(originalPlayer, newPlayer, ModAttributes.RESPAWN_SATURATION_RATE.get());
        }
    }

    private void copyAttribute(Player original, Player newPlayer, Attribute attribute) {
        AttributeInstance originalAttr = original.getAttribute(attribute);
        if (originalAttr != null && !Double.isNaN(originalAttr.getValue())) {
            AttributeInstance newAttr = newPlayer.getAttribute(attribute);
            if (newAttr != null) {
                newAttr.setBaseValue(originalAttr.getBaseValue());
            }
        }
    }

    private void setHealth(Player player) {

        if (player.getAttribute(ModAttributes.RESPAWN_HEALTH_RATE.get()) == null
        || Double.isNaN(player.getAttribute(ModAttributes.RESPAWN_HEALTH_RATE.get()).getValue()))
            return;

        player.setHealth((float)
                (player.getHealth() * Math.min(
                        Math.max(
                                player.getAttribute(ModAttributes.RESPAWN_HEALTH_RATE.get()).getValue(),
                                0.01),
                        1)));
    }

    private void setFood(Player player) {

        if (player.getAttribute(ModAttributes.RESPAWN_FOOD_RATE.get()) == null
                || Double.isNaN(player.getAttribute(ModAttributes.RESPAWN_FOOD_RATE.get()).getValue()))
            return;
        FoodData foodData = player.getFoodData();
        foodData.setFoodLevel((int)
                (foodData.getFoodLevel() * Math.min(
                        Math.max(
                                player.getAttribute(ModAttributes.RESPAWN_FOOD_RATE.get()).getValue(),
                                0),
                        1)));
    }

    private void setSaturation(Player player) {

        if (player.getAttribute(ModAttributes.RESPAWN_SATURATION_RATE.get()) == null
                || Double.isNaN(player.getAttribute(ModAttributes.RESPAWN_SATURATION_RATE.get()).getValue()))
            return;
        FoodData foodData = player.getFoodData();
        foodData.setSaturation((float) Math.min(
                (foodData.getSaturationLevel() * Math.max(
                        player.getAttribute(ModAttributes.RESPAWN_SATURATION_RATE.get()).getValue(),
                        0)),
                foodData.getFoodLevel()));
    }
}