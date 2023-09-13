package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import java.util.List;

//加速生长
public class AccelerateGrowthEvent extends FutureEvent {

    @SubscribeEvent
    public void onCropGrow(@Nonnull BlockEvent.CropGrowEvent.Pre evt) {
        if (!evt.getWorld().isRemote) {
            if (!this.isEnableEvent(evt.getWorld())) {
                return;
            }
            evt.setResult(Event.Result.ALLOW);
            @Nonnull List<EntityPlayer> entityPlayers = EntityUtil.getNearbyEntities(
                    EntityPlayer.class,
                    evt.getWorld(),
                    evt.getPos(),
                    64,
                    64,
                    64,
                    null
            );
            for (@Nonnull EntityPlayer entityPlayer : entityPlayers) {
                this.sendReminder(entityPlayer);
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
        return "accelerate_growth";
    }

}
