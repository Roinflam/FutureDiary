package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//‘Ï—™’œ∞≠
public class HematopoieticDisordersEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 20 == 0) {
                EntityPlayer entityPlayer = evt.player;
                if (entityPlayer.getHealth() < entityPlayer.getMaxHealth() && entityPlayer.getHealth() > 1) {
                    entityPlayer.setHealth(entityPlayer.getHealth() - (entityPlayer.getHealth() * 0.1f));
                    this.sendReminder(entityPlayer);
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
        return "hematopoietic_disorders";
    }

}
