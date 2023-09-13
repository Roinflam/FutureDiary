package pers.roinflam.futurediary;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.command.Commands;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.network.NetworkRegistryHandler;
import pers.roinflam.futurediary.proxy.CommonProxy;
import pers.roinflam.futurediary.utils.Reference;

import javax.annotation.Nonnull;

@Mod(modid = Reference.MOD_ID, useMetadata = true, guiFactory = "pers.roinflam.futurediary.gui.ConfigGuiFactory")
public class FutureDiary {
    @Mod.Instance
    public static FutureDiary instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    @Nonnull
    public static Commands commands = new Commands();

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent evt) {
        NetworkRegistryHandler.register();
    }

    @Mod.EventHandler
    public static void onFMLServerStopped(FMLServerStoppedEvent evt) {
        if (FutureEvent.futureData != null) {
            FutureEvent.futureData.saveFutureData();
        }
        FutureEvent.init();
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent evt) {

    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent evt) {

    }

    @SubscribeEvent
    @Mod.EventHandler
    public static void serverStarting(@Nonnull FMLServerStartingEvent evt) {
        evt.registerServerCommand(commands);
    }
}
