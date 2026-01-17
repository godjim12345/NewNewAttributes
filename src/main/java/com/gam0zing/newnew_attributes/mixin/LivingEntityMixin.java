package com.gam0zing.newnew_attributes.mixin;

import com.gam0zing.newnew_attributes.registry.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = LivingEntity.class, priority = 1000)
public abstract class LivingEntityMixin {

    @Shadow public abstract void heal(float pHealAmount);

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute pAttribute);

    @Shadow public abstract float getMaxHealth();

    @Unique
    public int newNewAttributes_1_20_1$absTick = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickMixin(CallbackInfo ci) {
        this.newNewAttributes_1_20_1$absTick++;
        if (this.newNewAttributes_1_20_1$absTick % 20 == 0) {
            this.newNewAttributes_1_20_1$absTick = 0;

            AttributeInstance healRate = this.getAttribute(ModAttributes.PERCENTAGE_NATURAL_HEALING.get());
            if (healRate != null) {
                double maxHealth = this.getMaxHealth();
                this.heal((float) (maxHealth * healRate.getValue()));
            }
        }
    }
}
