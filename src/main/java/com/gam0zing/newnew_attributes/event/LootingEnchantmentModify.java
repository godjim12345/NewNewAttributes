package com.gam0zing.newnew_attributes.event;

import com.gam0zing.newnew_attributes.registry.ModAttributes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootingEnchantmentModify {

    public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

    @SubscribeEvent
    public void scaleLootingEnchantment(LootingLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getDamageSource() == null || event.getDamageSource().getEntity() == null) return;
        if (event.getDamageSource().getEntity() instanceof LivingEntity source) {
            if (source.getAttribute(ModAttributes.LOOTING_ENCHANTMENT_SCALE.get()) == null) return;
            if (source.getAttribute(ModAttributes.LOOTING_ENCHANTMENT_ADDITION.get()) == null) return;
            event.setLootingLevel(
                    (int) ((event.getLootingLevel() + source.getAttribute(ModAttributes.LOOTING_ENCHANTMENT_ADDITION.get()).getValue()) * source.getAttribute(ModAttributes.LOOTING_ENCHANTMENT_SCALE.get()).getValue())
            );
        }
    }

    @SubscribeEvent
    public void scaleLooting(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getSource() == null || event.getSource().getEntity() == null) return;
        if (event.getSource().getEntity() instanceof LivingEntity source) {
            if (source.getAttribute(ModAttributes.LOOT_SCALE.get()) == null) return;
            double lootingScale = source.getAttribute(ModAttributes.LOOT_SCALE.get()).getValue();
            Vec3 pos = new Vec3(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
            //整数部分：大于2时，产生额外若干次掉落
            for (int i = 2; i <= lootingScale; i++) {
                for (ItemEntity itemEntity : event.getDrops()) {
                    event.getEntity().level().addFreshEntity(copyItemEntity(itemEntity));
                }
            }
            //小数部分：需要大于1，按概率产生额外1次掉落
            if (lootingScale > 1) {
                double lootingChance = lootingScale - (int) lootingScale;
                float random = RANDOM.nextFloat();
                if (lootingChance > random) {
                    for (ItemEntity itemEntity : event.getDrops()) {
                        event.getEntity().level().addFreshEntity(copyItemEntity(itemEntity));
                    }
                }
            }
            //如果小于1：将会影响原本的掉率
            else if (lootingScale < 1) {
                float random = RANDOM.nextFloat();
                if (lootingScale < random) {
                    event.getDrops().clear();
                }
            }
        }
    }

    ItemEntity copyItemEntity(ItemEntity old) {
        return new ItemEntity(old.level(), old.getX(), old.getY(), old.getZ(), old.getItem().copy());
    }
}
