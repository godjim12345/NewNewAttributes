package com.gam0zing.newnew_attributes;

import com.gam0zing.newnew_attributes.event.AutoShootHandler;
import com.gam0zing.newnew_attributes.registry.NNAttributes;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

        NNAttributes.ATTRIBUTES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new AutoShootHandler());
    }
}
