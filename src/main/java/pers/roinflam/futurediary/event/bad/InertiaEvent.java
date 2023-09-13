package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//不思进取
public class InertiaEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerPickupXp(@Nonnull PlayerPickupXpEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityXPOrb entityXPOrb = evt.getOrb();
            entityXPOrb.xpValue *= 0.33;
            this.sendReminder(evt.getEntityLiving());
        }
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
        return "inertia";
    }
}
