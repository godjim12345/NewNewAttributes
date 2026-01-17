package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DamageHandler {
    // 伤害倍数&限伤
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void scaleAndLimit(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        // 伤害倍数
        if (event.getSource().getEntity() instanceof LivingEntity source) {
            AttributeInstance damageScale = source.getAttribute(ModAttributes.DAMAGE_SCALE.get());
            if (damageScale != null) {
                event.setAmount(
                        event.getAmount() * (float) damageScale.getValue()
                );
            }
        }

        // 限伤
        LivingEntity living = event.getEntity();
        AttributeInstance hurtLimit = living.getAttribute(ModAttributes.HURT_LIMIT.get());

        if (hurtLimit != null && hurtLimit.getValue() >= 0) {
            event.setAmount(Math.min(event.getAmount(), (float) hurtLimit.getValue()));
        }
    }

    // 无敌帧，伤害上限移除MOD在此处为HIGHEST，在之后一个阶段计算可以最大程度保证兼容性
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void invulnerableTime(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        LivingEntity living = event.getEntity();
        AttributeInstance invulnerableTime = living.getAttribute(ModAttributes.INVULNERABLE_TIME.get());

        if (invulnerableTime != null) {
            living.invulnerableTime = (int) invulnerableTime.getValue();
        }
    }
}