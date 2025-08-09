package com.gam0zing.newnew_attributes.mixin;

import com.gam0zing.newnew_attributes.custom.ICrossbowItemExtension;
import com.gam0zing.newnew_attributes.registry.NNAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin implements ICrossbowItemExtension {

    @Unique
    private float chargingProgress = 0.0f;
    @Unique
    public boolean isReleasing = false;

    @Shadow
    private static float getPowerForTime(int useTicks, ItemStack stack) {
        //该实现不会调用，mixin会替换
        return 0.0f;
    }

    @Override
    public float getChargingProgress() {
        return this.chargingProgress;
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void onUseTickMixin(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {

        if (!level.isClientSide && entity instanceof Player && !isReleasing
        && entity.getAttribute(NNAttributes.AUTO_FIRE.get()) != null
        && entity.getAttribute(NNAttributes.AUTO_FIRE.get()).getValue() > 0) {

            int usedTicks = stack.getUseDuration() - remainingUseTicks;
            this.chargingProgress = getPowerForTime(usedTicks, stack);

            if (this.chargingProgress >= 1f) {
                isReleasing = true;
                entity.releaseUsingItem();
                isReleasing = false;
                this.chargingProgress = 0.0f;

            }
        }
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"))
    private void releaseUsingMixin(ItemStack stack, Level level, LivingEntity entity, int timeCharged, CallbackInfo ci) {
        //重置
        this.chargingProgress = 0.0f;
    }
}