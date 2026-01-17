package com.gam0zing.newnew_attributes;

import com.gam0zing.newnew_attributes.event.*;
import com.gam0zing.newnew_attributes.registry.ModAttributes;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(NewNewAttributes.MODID)
public class NewNewAttributes
{
    public static final String MODID = "newnew_attributes";
    private static final Logger LOGGER = LogUtils.getLogger();


    public NewNewAttributes(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModAttributes.ATTRIBUTES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new BowAutoShoot());
        MinecraftForge.EVENT_BUS.register(new PlayerRespawn());
        MinecraftForge.EVENT_BUS.register(new DamageHandler());
        MinecraftForge.EVENT_BUS.register(new LootingModify());
    }
}
