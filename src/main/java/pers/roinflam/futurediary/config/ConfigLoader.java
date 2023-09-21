package pers.roinflam.futurediary.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pers.roinflam.futurediary.utils.Reference;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
@Config(modid = Reference.MOD_ID)
public final class ConfigLoader {
    @Config.Comment("The frequency of update events, Minecraft is 1200 second a day")
    @Config.RangeInt(min = 0)
    public static int updateSecond = 16800;

    @Config.Comment("Set the frequency of prompting information after the event is triggered, the default is at most once every 10 minutes")
    @Config.RangeInt(min = 0)
    public static int sendReminderSecond = 600;

    @Config.Comment("Set the base generation probability of bad events. The first one is set to 1 if you want to have bad events for every future change, and 0.5 means that there is a half probability that at least one bad event will be generated.")
    @Config.RangeDouble(min = 0)
    public static double badEventBaseProbability = 1;

    @Config.Comment("Set when the first bad event occurs, reduce the base probability and then decide whether to generate the next bad event, for example, the base probability is 1, here the setting is 0.15, the probability of the first bad event is 100%, the second is 85%, and the third One is 70% and so on until the probability is equal to 0%")
    @Config.RangeDouble(min = 0)
    public static double badEventMultipleProbability = 0.19;

    @Config.Comment("Set the maximum number of bad events that can be automatically generated")
    @Config.RangeInt(min = 0)
    public static int badEventLimit = 6;

    @Config.Comment("Set the base generation probability of catastrophic events. The first one is set to 1 if you want to have catastrophic events for every future change, and 0.5 means that there is a half probability that at least one catastrophic event will be generated.")
    @Config.RangeDouble(min = 0)
    public static double catastrophicEventBaseProbability = 0.1;

    @Config.Comment("Set when the first catastrophic event occurs, reduce the base probability and then decide whether to generate the next catastrophic event, for example, the base probability is 1, here the setting is 0.33, the probability of the first catastrophic event is 100%, the second is 66%, and the third One is 33% and so on until the probability is equal to 0%")
    @Config.RangeDouble(min = 0)
    public static double catastrophicEventMultipleProbability = 0.033;

    @Config.Comment("Set the maximum number of catastrophic events that can be automatically generated")
    @Config.RangeInt(min = 0)
    public static int catastrophicEventLimit = 3;

    @Config.Comment("Set the base generation probability of good events. The first one is set to 1 if you want to have good events for every future change, and 0.5 means that there is a half probability that at least one good event will be generated.")
    @Config.RangeDouble(min = 0)
    public static double goodEventBaseProbability = 0.3;

    @Config.Comment("Set when the first good event occurs, reduce the base probability and then decide whether to generate the next good event, for example, the base probability is 1, here the setting is 0.1, the probability of the first good event is 100%, the second is 90%, and the third One is 80% and so on until the probability is equal to 0%")
    @Config.RangeDouble(min = 0)
    public static double goodEventMultipleProbability = 0.1;

    @Config.Comment("Set the maximum number of good events that can be automatically generated")
    @Config.RangeInt(min = 0)
    public static int goodEventLimit = 3;

    @Config.Comment("Set whether or not to enable all bad events (warning, the difficulty of survival will become very difficult with this on)")
    public static boolean enableAllBadEvents = false;

    @Config.Comment("Set whether or not to enable all catastrophic events (warning, the difficulty of survival will become very difficult with this on)")
    public static boolean enableAllCatastrophicEvents = false;

    @Config.Comment("Sets whether to enable all good events (warning, survival difficulty becomes very easy when enabled)")
    public static boolean enableAllGoodEvents = false;

    @Nonnull
    @Config.Comment("Set a world where future events are disabled")
    public static String[] disableWorld = {};

    @Nonnull
    @Config.Comment("Set disabled future events")
    public static String[] disableEvent = {};

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onConfigChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent evt) {
        if (evt.getModID().equals(Reference.MOD_ID)) {
            ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
        }
    }
}