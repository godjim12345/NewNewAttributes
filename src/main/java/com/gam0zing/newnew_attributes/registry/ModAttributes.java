package com.gam0zing.newnew_attributes.registry;

import com.gam0zing.newnew_attributes.NewNewAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, NewNewAttributes.MODID);

    public static final RegistryObject<Attribute> AUTO_FIRE = createAttribute("auto_fire", 0, 0, 1);
    public static final RegistryObject<Attribute> RESPAWN_HEALTH_RATE = createAttribute("respawn_health_rate", 1, 0, 1);
    public static final RegistryObject<Attribute> RESPAWN_FOOD_RATE = createAttribute("respawn_food_rate", 1, 0, 1);
    public static final RegistryObject<Attribute> RESPAWN_SATURATION_RATE = createAttribute("respawn_saturation_rate", 1, 0, 1024); //saturation
    public static final RegistryObject<Attribute> DAMAGE_SCALE = createAttribute("damage_scale", 1, 0, 1024);
    public static final RegistryObject<Attribute> LOOTING_ENCHANTMENT_SCALE = createAttribute("looting_enchantment_scale", 1, 0, 10);
    public static final RegistryObject<Attribute> LOOTING_ENCHANTMENT_ADDITION = createAttribute("looting_enchantment_addition", 0, 0, 255);
    public static final RegistryObject<Attribute> LOOT_SCALE = createAttribute("loot_scale", 1, 0, 255);

    public static RegistryObject<Attribute> createAttribute(final String id, double base, double min, double max) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute.name." + id, base, min, max).setSyncable(true));
    }

    @SubscribeEvent
    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
    }


}
