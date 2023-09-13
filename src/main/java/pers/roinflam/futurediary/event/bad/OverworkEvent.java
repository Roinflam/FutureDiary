package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;

import javax.annotation.Nonnull;
import java.util.HashMap;

//익작법똑
public class OverworkEvent extends FutureEvent {
    private final HashMap<String, Integer> breakNumber = new HashMap<>();

    @SubscribeEvent
    public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.getEntityPlayer();
            String uuidString = entityPlayer.getUniqueID().toString();
            if (breakNumber.getOrDefault(uuidString, 0) > 0) {
                evt.setNewSpeed(evt.getNewSpeed() * (1 - Math.min(breakNumber.getOrDefault(uuidString, 0), 75) * 0.01f));
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(@Nonnull BlockEvent.BreakEvent evt) {
        World world = evt.getWorld();
        if (!this.isEnableEvent(world)) {
            return;
        }
        EntityPlayer entityPlayer = evt.getPlayer();
        String uuidString = entityPlayer.getUniqueID().toString();
        breakNumber.put(uuidString, breakNumber.getOrDefault(uuidString, 0) + 1);
        if (breakNumber.get(uuidString) >= 25) {
            this.sendReminder(entityPlayer);
        }
        new SynchronizationTask(20 * 60 * 10) {

            @Override
            public void run() {
                if (breakNumber.getOrDefault(uuidString, 0) > 1) {
                    breakNumber.put(uuidString, breakNumber.get(uuidString) - 1);
                } else {
                    breakNumber.remove(uuidString);
                }
            }

        }.start();
    }

    @Override
    public boolean isBadEvent() {
        return true;
    }

    @Override
    public boolean isGoodEvent() {
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return false;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "overwork";
    }
}
