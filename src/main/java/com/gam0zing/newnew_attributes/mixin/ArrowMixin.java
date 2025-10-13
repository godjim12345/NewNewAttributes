package com.gam0zing.newnew_attributes.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.alchemy.Potion;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = Arrow.class, priority = 2000)
public abstract class ArrowMixin extends AbstractArrow {
    @Shadow
    private Potion potion;

    @Shadow
    @Final
    @Mutable
    private Set<MobEffectInstance> effects;

    @Shadow protected abstract void doPostHurtEffects(@NotNull LivingEntity pLiving);

    @Shadow public abstract void addEffect(MobEffectInstance pEffectInstance);

    protected ArrowMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    /**
     * @author gam0zing
     * @reason Add compatibility between arrow's magic damage with magic damage attribute
     */
    @Inject(method = "doPostHurtEffects", at = @At("HEAD"), cancellable = true)
    protected void doPostHurtEffects(LivingEntity pLiving, CallbackInfo ci) {
        super.doPostHurtEffects(pLiving);

        Set<MobEffectInstance> allEffects = new HashSet<>();
        if (!potion.getEffects().isEmpty()) allEffects.addAll(potion.getEffects());
        if (!effects.isEmpty()) allEffects.addAll(effects);

        //模仿原版药水云的实现方法，在施加瞬间效果时调用效果的applyInstantenousEffect方法
        for (MobEffectInstance effectInstance : allEffects) {
            if (effectInstance.getEffect().isInstantenous()) {
                effectInstance.getEffect().applyInstantenousEffect(this, this.getOwner(), pLiving, effectInstance.getAmplifier(), 1d);
            } else {
                pLiving.addEffect(new MobEffectInstance(effectInstance), this);
            }
        }
        ci.cancel();
    }
}
