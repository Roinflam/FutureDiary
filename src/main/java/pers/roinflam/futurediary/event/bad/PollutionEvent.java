package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import java.util.List;

//Œ€»æ
public class PollutionEvent extends FutureEvent {

    @SubscribeEvent
    public void onCropGrow(@Nonnull BlockEvent.CropGrowEvent.Pre evt) {
        if (!evt.getWorld().isRemote) {
            if (!this.isEnableEvent(evt.getWorld())) {
                return;
            }
            evt.setResult(Event.Result.DENY);
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
        return "pollution";
    }

}
