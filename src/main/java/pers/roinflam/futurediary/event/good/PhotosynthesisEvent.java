package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//光合作用
public class PhotosynthesisEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.player;
            if (world.getTotalWorldTime() % 20 == 0 && world.isDaytime() && world.canSeeSky(entityPlayer.getPosition())) {
                entityPlayer.heal(entityPlayer.getMaxHealth() * 0.01f);
                FoodStats foodStats = entityPlayer.getFoodStats();
                if (foodStats.needFood()) {
                    foodStats.setFoodLevel(foodStats.getFoodLevel() + 1);
                }
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
        return "photosynthesis";
    }
}
