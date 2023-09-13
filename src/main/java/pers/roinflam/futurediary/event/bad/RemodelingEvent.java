package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//—™»‚÷ÿÀ‹
public class RemodelingEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingHeal(@Nonnull LivingHealEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (RandomUtil.percentageChance(20)) {
                EntityLivingBase healer = evt.getEntityLiving();
                if (healer.getHealth() < healer.getMaxHealth() * 0.95) {
                    healer.attackEntityFrom(DamageSource.GENERIC, evt.getAmount() * 2);
                    this.sendReminder(healer);
                    evt.setCanceled(true);
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
        return "remodeling";
    }
}
