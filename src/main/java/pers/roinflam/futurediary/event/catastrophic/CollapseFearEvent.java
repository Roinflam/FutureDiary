package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;

import javax.annotation.Nonnull;
import java.util.HashSet;

//Ì®Ëõ¿Ö¾å
public class CollapseFearEvent extends FutureEvent {
    private final HashSet<String> kickCoolding = new HashSet<>();

    public void quitGame(@Nonnull EntityPlayer entityPlayer, @Nonnull ITextComponent textComponent) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {

            @Override
            public void run() {
                MinecraftServer server = entityPlayer.getServer();
                server.initiateShutdown();
                Minecraft client = Minecraft.getMinecraft();
                client.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", textComponent));
            }

        });
    }

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                String uuidString = entityPlayer.getUniqueID().toString();
                if (entityPlayer.world.getLight(entityPlayer.getPosition()) < 15 && !kickCoolding.contains(uuidString)) {
                    TextComponentTranslation textComponentString = new TextComponentTranslation("event.futurediary." + getEventName() + ".message");
                    textComponentString.getStyle().setColor(TextFormatting.DARK_RED);
                    if (entityPlayer.getServer().isSinglePlayer() && entityPlayer instanceof EntityPlayerMP) {
                        quitGame(entityPlayer, textComponentString);
                    } else {
                        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
                        entityPlayerMP.connection.disconnect(textComponentString);
                    }
                    kickCoolding.add(uuidString);
                    this.sendReminder(entityPlayer);
                    new SynchronizationTask(12000) {

                        @Override
                        public void run() {
                            kickCoolding.remove(entityPlayer.getUniqueID().toString());
                        }

                    }.start();
                }
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return false;
    }

    @Override
    public boolean isGoodEvent() {
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return true;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "collapse_fear";
    }
}
