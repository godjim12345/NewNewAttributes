package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.NNAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BowAutoShootHandler {

    volatile ItemStack heldItem;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        if (player.level().isClientSide) return;
        if (!(player.getAttribute(NNAttributes.AUTO_FIRE.get()).getValue() > 0)) return;

        heldItem = player.getUseItem();

        if (heldItem.getItem() instanceof BowItem) {
            //检测拉力
            float pull = BowItem.getPowerForTime(player.getTicksUsingItem());
            //检测使用时间比例
            float progress = (float)player.getTicksUsingItem() / ((BowItem)heldItem.getItem()).getUseDuration(heldItem);

            System.out.println(pull + " " + progress);

            if (pull >= 1f || progress >= 1f) {
                player.releaseUsingItem();
            }
        }
    }
}