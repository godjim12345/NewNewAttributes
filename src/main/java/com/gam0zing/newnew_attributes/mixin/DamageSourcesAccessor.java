package com.gam0zing.newnew_attributes.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DamageSources.class)
public interface DamageSourcesAccessor {
    @Accessor("damageTypes")
    Registry<DamageType> getDamageTypes();
}
