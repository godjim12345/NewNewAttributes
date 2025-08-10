package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.NNAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BowAutoShootHandler {

    volatile ItemStack usingItem;
    //服务端标记位
    volatile boolean severFlag = false;
    //客户端标记位
    volatile boolean clientFlag = false;


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        //在服务端和客户端同时捕捉Player和UseItem
        Player player = event.player;
        usingItem = player.getUseItem();

        //**********防中断逻辑**********//
        //核心出装
        //InteractionHand hand = serverPlayer.getUsedItemHand();
        //serverPlayer.startUsingItem(hand);

        //客户端
        if (player.level().isClientSide) {
            //防中断步骤2：来到赋值标记后的下一刻
            //如果检测为弩，则赋值true，否则赋值false
            if (usingItem.getItem() instanceof CrossbowItem) clientFlag = true;
            else clientFlag = false;

            //System.out.println("客户端：" + clientFlag);
        }
        //服务端
        if (!player.level().isClientSide) {
            //防中断步骤1：服务端先赋值标记
            //如果检测为弩，则赋值true，否则赋值false
            if (usingItem.getItem() instanceof CrossbowItem) severFlag = true;
            else severFlag = false;

            //System.out.println("服务端：" + severFlag);
        }

        //自动扳机核心逻辑，在服务端中执行
        if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            //检查自动扳机属性
            if (!(serverPlayer.getAttribute(NNAttributes.AUTO_FIRE.get()).getValue() > 0)) return;

            //防中断步骤3：判断标记位并重启服务器物品使用
            if (clientFlag && !severFlag) {

                //System.out.println("防中断启用");

                InteractionHand hand = serverPlayer.getUsedItemHand();
                serverPlayer.startUsingItem(hand);
            }

            //弩
            if (usingItem.getItem() instanceof CrossbowItem) {
                //检查蓄力
                int useTicks = player.getTicksUsingItem();
                int chargeDuration = CrossbowItem.getChargeDuration(usingItem);
                float progress = (float) useTicks / (float) chargeDuration;
                //这一步的作用是松开蓄力并让弩上好弦，在长按的情况下，下一刻检测到使用会自动激发
                if (progress >= 1f) {
                    player.releaseUsingItem();
                }
            }
            //弓
            else if (usingItem.getItem() instanceof BowItem) {
                //检查蓄力
                float pull = BowItem.getPowerForTime(player.getTicksUsingItem());
                //弓松开就等于发射
                if (pull >= 1f) {
                    player.releaseUsingItem();
                }
            }
        }
    }
}