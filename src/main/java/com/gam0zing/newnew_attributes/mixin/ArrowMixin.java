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
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = Arrow.class, priority = 2000)
public abstract class ArrowMixin extends AbstractArrow {
    @Shadow
    private Potion potion;

    @Shadow
    @Final
    @Mutable
    private Set<MobEffectInstance> effects;

    @Shadow protected abstract void doPostHurtEffects(LivingEntity pLiving);

    protected ArrowMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    private void newNewAttributes1201$applyHarmDamage(LivingEntity pLiving, Registry<DamageType> damageTypes, MobEffectInstance mobEffectInstance) {
        pLiving.hurt(new DamageSource(damageTypes.getHolderOrThrow(DamageTypes.MAGIC), pLiving.getLastAttacker()),
                6 << mobEffectInstance.getAmplifier());
    }
    @Unique
    private boolean newNewAttributes1201$applyDamage(LivingEntity pLiving, Registry<DamageType> damageTypes, MobEffectInstance mobEffectInstance) {
        //非亡灵，瞬间伤害
        if (mobEffectInstance.getEffect() == MobEffects.HARM && !pLiving.isInvertedHealAndHarm()) {
            //应用伤害
            newNewAttributes1201$applyHarmDamage(pLiving, damageTypes, mobEffectInstance);
            //跳过原版逻辑
            return true;
        }
        //亡灵，瞬间治疗
        else if (mobEffectInstance.getEffect() == MobEffects.HEAL && pLiving.isInvertedHealAndHarm()) {
            //应用伤害
            newNewAttributes1201$applyHarmDamage(pLiving, damageTypes, mobEffectInstance);
            //跳过原版逻辑
            return true;
        }
        return false;
    }
    /**
     * @author gam0zing
     * @reason Add compatibility between arrow's magic damage with magic damage attribute
     */
    @Inject(method = "doPostHurtEffects", at = @At("HEAD"), cancellable = true)
    protected void doPostHurtEffects(LivingEntity pLiving, CallbackInfo ci) {
        super.doPostHurtEffects(pLiving);

        // 使用访问器接口获取damageTypes
        Registry<DamageType> damageTypes = ((DamageSourcesAccessor)pLiving.damageSources()).getDamageTypes();
        //获取箭矢的发射者，或箭矢本身
        Entity entity = this.getEffectSource();

        //原版方法修改，加入对瞬间伤害的筛选
        for(MobEffectInstance mobEffectInstance : this.potion.getEffects()) {
            //检测是否需要逻辑替换
            if (newNewAttributes1201$applyDamage(pLiving, damageTypes, mobEffectInstance)) continue;

            //原版逻辑，正常施加药水效果
            pLiving.addEffect(new MobEffectInstance(mobEffectInstance.getEffect(), Math.max(mobEffectInstance.mapDuration((p_268168_) -> {
                return p_268168_ / 8;
            }), 1), mobEffectInstance.getAmplifier(), mobEffectInstance.isAmbient(), mobEffectInstance.isVisible()), entity);
        }

        //原版方法修改，加入对瞬间伤害的筛选
        if (!this.effects.isEmpty()) {
            for(MobEffectInstance mobEffectInstance : this.effects) {
                //检测是否需要逻辑替换
                if (newNewAttributes1201$applyDamage(pLiving, damageTypes, mobEffectInstance)) continue;

                //原版逻辑，正常施加药水效果
                pLiving.addEffect(mobEffectInstance, entity);
            }
        }

        ci.cancel();
    }
}
