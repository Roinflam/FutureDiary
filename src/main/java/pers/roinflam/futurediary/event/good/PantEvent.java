package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//ºôÎü»ØÑª
public class PantEvent extends FutureEvent {
    private final List<String> battle = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.player;
            if (world.getTotalWorldTime() % 20 == 0 && !battle.contains(entityPlayer.getUniqueID().toString())) {
                entityPlayer.heal(entityPlayer.getHealth() * 0.1f);
                this.sendReminder(entityPlayer);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                String uuidString = entityPlayer.getUniqueID().toString();
                battle.add(uuidString);
                new SynchronizationTask(200) {

                    @Override
                    public void run() {
                        battle.remove(uuidString);
                    }

                }.start();
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) damageSource.getTrueSource();
                String uuidString = entityPlayer.getUniqueID().toString();
                battle.add(uuidString);
                new SynchronizationTask(200) {

                    @Override
                    public void run() {
                        battle.remove(uuidString);
                    }

                }.start();
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return false;
    }

    @Override
    public boolean isGoodEvent() {
        return true;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return false;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "pant";
    }

}
