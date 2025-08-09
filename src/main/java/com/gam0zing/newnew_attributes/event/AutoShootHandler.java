package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.NNAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoShootHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        if (player.level().isClientSide) return;
        if (!(player.getAttribute(NNAttributes.AUTO_FIRE.get()).getValue() > 0)) return;

        ItemStack heldItem = player.getUseItem();

        if (heldItem.getItem() instanceof BowItem) {
            //检测拉力
            float pull = BowItem.getPowerForTime(player.getTicksUsingItem());
            //检测使用时间比例
            float progress = (float)player.getTicksUsingItem() / ((BowItem)heldItem.getItem()).getUseDuration(heldItem);

            if (pull >= 1f || progress >= 1f) {
                player.releaseUsingItem();
            }
        }
        else if (heldItem.getItem() instanceof CrossbowItem) {
            //检测装填状态
            boolean isCharged = CrossbowItem.isCharged(heldItem);
            //检测使用时间比例
            float progress = (float)player.getTicksUsingItem() / ((CrossbowItem)heldItem.getItem()).getUseDuration(heldItem);

            if (isCharged || progress >= 1f) { // 25 ticks是原版弩的装填时间
                player.releaseUsingItem();
            }
        }
    }
}