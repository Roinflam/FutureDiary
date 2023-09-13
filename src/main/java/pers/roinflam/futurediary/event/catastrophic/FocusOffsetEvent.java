package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//×¨×¢Ê§µ÷
public class FocusOffsetEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 1500 == 0 && RandomUtil.percentageChance(25)) {
                EntityPlayer entityPlayer = evt.player;
                boolean useItem = false;
                if (!entityPlayer.getHeldItemMainhand().isEmpty()) {
                    entityPlayer.getHeldItemMainhand().useItemRightClick(world, entityPlayer, EnumHand.MAIN_HAND);
                    useItem = true;
                } else if (!entityPlayer.getHeldItemOffhand().isEmpty()) {
                    entityPlayer.getHeldItemOffhand().useItemRightClick(world, entityPlayer, EnumHand.OFF_HAND);
                    useItem = true;
                }
                if (useItem) {
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
        return "focus_offset";
    }

}
