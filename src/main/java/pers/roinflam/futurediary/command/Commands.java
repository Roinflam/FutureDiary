package pers.roinflam.futurediary.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.init.FutureDiaryEvents;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Commands extends CommandBase {

    @Nonnull
    @Override
    public String getName() {
        return "FutureDiary";
    }

    @Nonnull
    @Override
    public String getUsage(ICommandSender sender) {
        return "/FutureDiary";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, @Nonnull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("show")) {
                FutureEvent.show(sender);
                return;
            }
            if (args[0].equalsIgnoreCase("update")) {
                FutureEvent.updateEvent(false);
                return;
            }
            if (args[0].equalsIgnoreCase("clear")) {
                FutureEvent.updateEvent(true);
                return;
            }
            if (args[0].equalsIgnoreCase("list")) {
                for (FutureEvent futureEvent : FutureDiaryEvents.EVENTS) {
                    String eventName = futureEvent.getEventName();
                    TextComponentTranslation name = new TextComponentTranslation("event.futurediary." + eventName);
                    name.getStyle();
                    if (futureEvent.isBadEvent()) {
                        name.getStyle().setColor(TextFormatting.RED);
                        TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nameBad", name);
                        textComponentString.getStyle().setColor(TextFormatting.GRAY);
                        sender.sendMessage(textComponentString.appendText(": " + eventName));
                    } else if (futureEvent.isCatastrophicEvent()) {
                        name.getStyle().setColor(TextFormatting.DARK_RED);
                        TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nameCatastrophic", name);
                        textComponentString.getStyle().setColor(TextFormatting.GRAY);
                        sender.sendMessage(textComponentString.appendText(": " + eventName));
                    } else if (futureEvent.isGoodEvent()) {
                        name.getStyle().setColor(TextFormatting.GREEN);
                        TextComponentTranslation textComponentString = new TextComponentTranslation("message.futurediary.nameGood", name);
                        textComponentString.getStyle().setColor(TextFormatting.GRAY);
                        sender.sendMessage(textComponentString.appendText(": " + eventName));
                    }
                }
                return;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                String eventName = args[1];
                for (FutureEvent futureEvent : FutureDiaryEvents.EVENTS) {
                    if (futureEvent.getEventName().equals(eventName)) {
                        NBTTagList badEvents = FutureEvent.futureData.getBadEvents();
                        NBTTagList catastrophicEvents = FutureEvent.futureData.getCatastrophicEvents();
                        NBTTagList goodEvents = FutureEvent.futureData.getGoodEvents();
                        for (int i = 0; i < badEvents.tagCount(); i++) {
                            if (badEvents.getStringTagAt(i).equals(eventName)) {
                                sender.sendMessage(new TextComponentTranslation("message.command.futurediary.exists"));
                                return;
                            }
                        }
                        for (int i = 0; i < catastrophicEvents.tagCount(); i++) {
                            if (catastrophicEvents.getStringTagAt(i).equals(eventName)) {
                                sender.sendMessage(new TextComponentTranslation("message.command.futurediary.exists"));
                                return;
                            }
                        }
                        for (int i = 0; i < goodEvents.tagCount(); i++) {
                            if (goodEvents.getStringTagAt(i).equals(eventName)) {
                                sender.sendMessage(new TextComponentTranslation("message.command.futurediary.exists"));
                                return;
                            }
                        }
                        if (futureEvent.isBadEvent()) {
                            badEvents.appendTag(new NBTTagString(futureEvent.getEventName()));
                            FutureEvent.futureData.setBadEvents(badEvents);
                        } else if (futureEvent.isCatastrophicEvent()) {
                            catastrophicEvents.appendTag(new NBTTagString(futureEvent.getEventName()));
                            FutureEvent.futureData.setCatastrophicEvents(catastrophicEvents);
                        } else if (futureEvent.isGoodEvent()) {
                            goodEvents.appendTag(new NBTTagString(futureEvent.getEventName()));
                            FutureEvent.futureData.setGoodEvents(goodEvents);
                        }
                        FutureEvent.futureData.saveFutureData();
                        sender.sendMessage(new TextComponentTranslation("message.command.futurediary.success"));
                        return;
                    }
                }
                sender.sendMessage(new TextComponentTranslation("message.command.futurediary.notFound"));
                return;
            }
        }
        sender.sendMessage(new TextComponentTranslation("message.command.futurediary.show"));
        sender.sendMessage(new TextComponentTranslation("message.command.futurediary.update"));
        sender.sendMessage(new TextComponentTranslation("message.command.futurediary.clear"));
        sender.sendMessage(new TextComponentTranslation("message.command.futurediary.list"));
        sender.sendMessage(new TextComponentTranslation("message.command.futurediary.add"));
    }

    // 返回指令的别名列表
    @Nonnull
    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("fd");
        return aliases;
    }

    // 返回指令是否需要OP权限
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
