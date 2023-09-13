package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//รนิห
public class BadLuckEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(@Nonnull LivingDropsEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase dead = evt.getEntityLiving();
            if (dead.isNonBoss()) {
                if (RandomUtil.percentageChance(10)) {
                    evt.setCanceled(true);
                    if (evt.getSource().getTrueSource() instanceof EntityPlayer) {
                        this.sendReminder((EntityLivingBase) evt.getSource().getTrueSource());
                    }
                }
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
        return "badluck";
    }
}
