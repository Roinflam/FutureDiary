package pers.roinflam.futurediary.event;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.config.ConfigLoader;
import pers.roinflam.futurediary.data.FutureData;
import pers.roinflam.futurediary.init.FutureDiaryEvents;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber
public abstract class FutureEvent {
    @Nonnull
    public static HashMap<String, HashMap<String, Integer>> reminderCoolding = new HashMap<>();
    @Nullable
    public static FutureData futureData = null;
    @Nullable
    public static String saveName = null;
    public static int totalSecond = 0;

    static {
        new SynchronizationTask(20, 20) {

            @Override
            public void run() {
                for (String uuid : new ArrayList<>(reminderCoolding.keySet())) {
                    HashMap<String, Integer> coolding = reminderCoolding.getOrDefault(uuid, new HashMap<>());
                    for (String eventName : new ArrayList<>(coolding.keySet())) {
                        coolding.put(eventName, coolding.get(eventName) - 1);
                        if (coolding.get(eventName) <= 0) {
                            coolding.remove(eventName);
                        }
                    }
                    if (coolding.size() > 0) {
                        reminderCoolding.put(uuid, coolding);
                    } else {
                        reminderCoolding.remove(uuid);
                    }
                }
            }

        }.start();
        new SynchronizationTask(20 * 60 * 15, 20 * 60 * 15) {

            @Override
            public void run() {
                reminderCoolding.clear();
            }

        }.start();
    }

    public FutureEvent() {
        if (!Arrays.asList(ConfigLoader.disableEvent).contains(getEventName())) {
            MinecraftForge.EVENT_BUS.register(this);
            FutureDiaryEvents.EVENTS.add(this);
        }
    }

    @SubscribeEvent
    public static void onFutureWorldLoad(@Nonnull WorldEvent.Load evt) {
        if (!evt.getWorld().isRemote) {
            World world = evt.getWorld();
            if (futureData == null || saveName == null) {
                futureData = new FutureData(world);
                saveName = world.getSaveHandler().getWorldDirectory().getName();
                totalSecond = futureData.getTotalSecond();
                futureData.saveFutureData();
                System.out.println("Successfully loaded FutureEvents are as follows");
                for (FutureEvent futureEvent : FutureDiaryEvents.EVENTS) {
                    System.out.println(" - " + futureEvent.getEventName());
                }
            }
        }
    }

    public static void updateEvent(boolean clear) {
        List<FutureEvent> futureEventList = new ArrayList<>(FutureDiaryEvents.EVENTS);
        Collections.shuffle(futureEventList);

        NBTTagList badEvents = new NBTTagList();
        NBTTagList catastrophicEvents = new NBTTagList();
        NBTTagList goodEvents = new NBTTagList();

        if (!clear) {
            int badEventNumber = 0;
            int catastrophicEventNumber = 0;
            int goodEventNumber = 0;
            for (FutureEvent futureEvent : futureEventList) {
                if (futureEvent.isBadEvent() && badEventNumber < ConfigLoader.badEventLimit) {
                    if (RandomUtil.percentageChance((ConfigLoader.badEventBaseProbability - ConfigLoader.badEventMultipleProbability * badEventNumber) * 100)) {
                        badEvents.appendTag(new NBTTagString(futureEvent.getEventName()));
                        badEventNumber++;
                        continue;
                    } else {
                        badEventNumber = ConfigLoader.badEventLimit;
                    }
                }
                if (futureEvent.isCatastrophicEvent() && catastrophicEventNumber < ConfigLoader.catastrophicEventLimit) {
                    if (RandomUtil.percentageChance((ConfigLoader.catastrophicEventBaseProbability - ConfigLoader.catastrophicEventMultipleProbability * catastrophicEventNumber) * 100)) {
                        catastrophicEvents.appendTag(new NBTTagString(futureEvent.getEventName()));
                        catastrophicEventNumber++;
                        continue;
                    } else {
                        catastrophicEventNumber = ConfigLoader.catastrophicEventLimit;
                    }
                }
                if (futureEvent.isGoodEvent() && goodEventNumber < ConfigLoader.goodEventLimit) {
                    if (RandomUtil.percentageChance((ConfigLoader.goodEventBaseProbability - ConfigLoader.goodEventMultipleProbability * goodEventNumber) * 100)) {
                        goodEvents.appendTag(new NBTTagString(futureEvent.getEventName()));
                        goodEventNumber++;
                    } else {
                        goodEventNumber = ConfigLoader.goodEventLimit;
                    }
                }
            }
        }
        futureData.setBadEvents(badEvents);
        futureData.setCatastrophicEvents(catastrophicEvents);
        futureData.setGoodEvents(goodEvents);
        futureData.saveFutureData();

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        PlayerList playerList = server.getPlayerList();
        for (EntityPlayerMP player : playerList.getPlayers()) {
            ITextComponent textComponentTranslation = new TextComponentTranslation("message.futurediary.updateEvent");
            textComponentTranslation.getStyle().setColor(TextFormatting.DARK_RED);
            player.sendMessage(textComponentTranslation);
        }
    }

    @SubscribeEvent
    public static void onFutureWorldTick(@Nonnull TickEvent.WorldTickEvent evt) {
        if (!evt.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.world;
            if (world.getTotalWorldTime() % 20 == 0) {
                if (futureData.isFirst()) {
                    futureData.setFirst(false);
                    updateEvent(false);
                }
                futureData.setTotalSecond(++totalSecond);
            }
            if (world.getTotalWorldTime() % (ConfigLoader.updateSecond * 20L) == 0) {
                updateEvent(false);
            } else if (world.getTotalWorldTime() % 6000 == 0) {
                futureData.saveFutureData();
            }
        }
    }

    public static void show(@Nonnull ICommandSender sender) {
        boolean hasEvent = false;
        NBTTagList badEvents = FutureEvent.futureData.getBadEvents();
        NBTTagList catastrophicEvents = FutureEvent.futureData.getCatastrophicEvents();
        NBTTagList goodEvents = FutureEvent.futureData.getGoodEvents();
        for (int i = 0; i < badEvents.tagCount(); i++) {
            if (!hasEvent) {
                TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nowEvent");
                textComponentString.getStyle().setColor(TextFormatting.RED).setBold(true);
                sender.sendMessage(textComponentString);
                hasEvent = true;
            }
            String eventName = badEvents.getStringTagAt(i);
            TextComponentTranslation name = new TextComponentTranslation("event.futurediary." + eventName);
            name.getStyle().setColor(TextFormatting.RED);
            TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nameBad", name);
            textComponentString.getStyle().setColor(TextFormatting.GRAY);
            sender.sendMessage(textComponentString);
            textComponentString = new TextComponentTranslation("event.futurediary." + eventName + ".introducetion");
            textComponentString.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textComponentString);
        }
        for (int i = 0; i < catastrophicEvents.tagCount(); i++) {
            if (!hasEvent) {
                TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nowEvent");
                textComponentString.getStyle().setColor(TextFormatting.RED).setBold(true);
                sender.sendMessage(textComponentString);
                hasEvent = true;
            }
            String eventName = catastrophicEvents.getStringTagAt(i);
            TextComponentTranslation name = new TextComponentTranslation("event.futurediary." + eventName);
            name.getStyle().setColor(TextFormatting.DARK_RED);
            TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nameCatastrophic", name);
            textComponentString.getStyle().setColor(TextFormatting.GRAY);
            sender.sendMessage(textComponentString);
            textComponentString = new TextComponentTranslation("event.futurediary." + eventName + ".introducetion");
            textComponentString.getStyle().setColor(TextFormatting.DARK_RED);
            sender.sendMessage(textComponentString);
        }
        for (int i = 0; i < goodEvents.tagCount(); i++) {
            if (!hasEvent) {
                TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nowEvent");
                textComponentString.getStyle().setColor(TextFormatting.RED).setBold(true);
                sender.sendMessage(textComponentString);
                hasEvent = true;
            }
            String eventName = goodEvents.getStringTagAt(i);
            TextComponentTranslation name = new TextComponentTranslation("event.futurediary." + eventName);
            name.getStyle().setColor(TextFormatting.GREEN);
            TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nameGood", name);
            textComponentString.getStyle().setColor(TextFormatting.GRAY);
            sender.sendMessage(textComponentString);
            textComponentString = new TextComponentTranslation("event.futurediary." + eventName + ".introducetion");
            textComponentString.getStyle().setColor(TextFormatting.GREEN);
            sender.sendMessage(textComponentString);
        }
        if (!hasEvent) {
            TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.notEvent");
            textComponentString.getStyle().setColor(TextFormatting.DARK_RED).setBold(true);
            sender.sendMessage(textComponentString);
        }
    }

    public static void init() {
        futureData = null;
        saveName = null;
        totalSecond = 0;
    }

    public abstract boolean isBadEvent();

    public abstract boolean isGoodEvent();

    public abstract boolean isCatastrophicEvent();

    public abstract String getEventName();

    @Nonnull
    public String getEventIntroducetion() {
        return getEventName() + ".introducetion";
    }

    @Nonnull
    public String getEventMessage() {
        return getEventName() + ".message";
    }

    private void sendMessage(@Nonnull EntityLivingBase entityLivingBase) {
        TextComponentTranslation textComponentString = new TextComponentTranslation("event.futurediary." + getEventMessage());
        textComponentString.getStyle().setColor(TextFormatting.DARK_RED);
        entityLivingBase.sendMessage(textComponentString);
    }

    public void sendReminder(EntityLivingBase entityLivingBase) {
        sendReminder(entityLivingBase, 1);
    }

    public void sendReminder(EntityLivingBase entityLivingBase, double messageCoolding) {
        if (entityLivingBase instanceof EntityPlayer) {
            HashMap<String, Integer> coolding = reminderCoolding.getOrDefault(entityLivingBase.getUniqueID().toString(), new HashMap<>());
            if (!coolding.containsKey(getEventName())) {
                sendMessage(entityLivingBase);
                coolding.put(getEventName(), (int) (ConfigLoader.sendReminderSecond * messageCoolding));
                reminderCoolding.put(entityLivingBase.getUniqueID().toString(), coolding);
            }
        }
    }


    public boolean isEnableWorld(@Nonnull World world) {
        return !Arrays.asList(ConfigLoader.disableWorld).contains(String.valueOf(world.provider.getDimension()));
    }

    public boolean isEnableEvent(@Nonnull World world) {
        if (Arrays.asList(ConfigLoader.disableEvent).contains(getEventName())) {
            return false;
        }
        if (!isEnableWorld(world)) {
            return false;
        }
        if (this.isBadEvent() && ConfigLoader.enableAllBadEvents) {
            return isEnableWorld(world);
        }
        if (this.isCatastrophicEvent() && ConfigLoader.enableAllCatastrophicEvents) {
            return isEnableWorld(world);
        }
        if (this.isGoodEvent() && ConfigLoader.enableAllGoodEvents) {
            return isEnableWorld(world);
        }
        if (this.isBadEvent()) {
            NBTTagList badEvents = futureData.getBadEvents();
            for (int i = 0; i < badEvents.tagCount(); i++) {
                if (badEvents.getStringTagAt(i).equals(this.getEventName())) {
                    return true;
                }
            }
        }
        if (this.isCatastrophicEvent()) {
            NBTTagList catastrophicEvents = futureData.getCatastrophicEvents();
            for (int i = 0; i < catastrophicEvents.tagCount(); i++) {
                if (catastrophicEvents.getStringTagAt(i).equals(this.getEventName())) {
                    return true;
                }
            }
        }
        if (this.isGoodEvent()) {
            NBTTagList goodEvents = futureData.getGoodEvents();
            for (int i = 0; i < goodEvents.tagCount(); i++) {
                if (goodEvents.getStringTagAt(i).equals(this.getEventName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(futureData.isFirst(), futureData.getBadEvents(), futureData.getCatastrophicEvents(), futureData.getGoodEvents(), totalSecond);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FutureEvent)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return this.getEventName().equals(((FutureEvent) obj).getEventName());
    }
}
