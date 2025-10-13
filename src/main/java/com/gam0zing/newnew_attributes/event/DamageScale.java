package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DamageScale {
    @SubscribeEvent
    public void scaleDamage(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getSource().getEntity() == null) return;
        if (event.getSource().getEntity() instanceof LivingEntity source) {
            if (source.getAttribute(ModAttributes.DAMAGE_SCALE.get()) == null) return;
            event.setAmount(
                    event.getAmount() * (float) source.getAttribute(ModAttributes.DAMAGE_SCALE.get()).getValue()
            );
        }
    }
}