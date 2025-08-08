package com.gam0zing.newnew_attributes.registry;

import com.gam0zing.newnew_attributes.NewNewAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NNAttributes {
    public static final int MAX = 1024;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, NewNewAttributes.MODID);

    public static final RegistryObject<Attribute> AUTO_FIRE = createAttribute("auto_fire");

    public static RegistryObject<Attribute> createAttribute(final String id) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute.name." + id, 0, 0, MAX).setSyncable(true));
    }

    @SubscribeEvent
    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
    }


}
