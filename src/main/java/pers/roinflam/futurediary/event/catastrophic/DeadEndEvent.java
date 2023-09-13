package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//DEAD END
public class DeadEndEvent extends FutureEvent {
    public static final DamageSource DEAD_END = new DamageSource("dead_end").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer && RandomUtil.percentageChance(1)) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                DeadEndEvent.this.sendReminder(entityPlayer, 0.1);
                new SynchronizationTask() {

                    @Override
                    public void run() {
                        if (entityPlayer.isEntityAlive()) {
                            entityPlayer.onDeath(DEAD_END);
                            entityPlayer.setHealth(0);
                        }
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
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return true;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "dead_end";
    }
}
