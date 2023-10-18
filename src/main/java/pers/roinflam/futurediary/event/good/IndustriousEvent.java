package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//ÇÚÀÍ
public class IndustriousEvent extends FutureEvent {

    @SubscribeEvent
    public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            evt.setNewSpeed((float) (evt.getNewSpeed() * 3.5));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(@Nonnull BlockEvent.BreakEvent evt) {
        World world = evt.getWorld();
        if (!this.isEnableEvent(world)) {
            return;
        }
        EntityPlayer entityPlayer = evt.getPlayer();
        this.sendReminder(entityPlayer);
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
        return "industrious";
    }
}
